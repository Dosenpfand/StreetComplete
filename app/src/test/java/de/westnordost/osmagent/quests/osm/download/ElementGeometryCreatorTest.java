package de.westnordost.osmagent.quests.osm.download;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.westnordost.osmagent.quests.osm.ElementGeometry;
import de.westnordost.osmapi.map.data.Element;
import de.westnordost.osmapi.map.data.LatLon;
import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.OsmLatLon;
import de.westnordost.osmapi.map.data.OsmNode;
import de.westnordost.osmapi.map.data.OsmRelation;
import de.westnordost.osmapi.map.data.OsmRelationMember;
import de.westnordost.osmapi.map.data.OsmWay;
import de.westnordost.osmapi.map.data.Relation;
import de.westnordost.osmapi.map.data.RelationMember;
import de.westnordost.osmapi.map.data.Way;

public class ElementGeometryCreatorTest extends TestCase implements GeometryMapDataProvider
{
	private static final LatLon
			P0 = new OsmLatLon(0d,0d),
			P1 = new OsmLatLon(1d,1d),
			P2 = new OsmLatLon(2d,2d),
			P3 = new OsmLatLon(3d,3d);

	private static final Node
			N0 =  new OsmNode(0L,0,P0,null,null),
			N1 =  new OsmNode(1L,0,P1,null,null),
			N2 =  new OsmNode(2L,0,P2,null,null),
			N3 =  new OsmNode(3L,0,P3,null,null);

	private static final Map<String,String>
			wayArea = new HashMap<>(),
			relationArea = new HashMap<>();
	static {
		wayArea.put("area", "yes");
		relationArea.put("type","multipolygon");
	}

	private static final Way
			W0 = new OsmWay(0L,0, Arrays.asList(0L,1L),null,null),
			W1 = new OsmWay(1L,0,Arrays.asList(1L,2L,0L),null,null),
			W2 = new OsmWay(2L,0,Arrays.asList(0L,1L,2L,0L),wayArea,null),
			W3 = new OsmWay(3L,0,Arrays.asList(3L,2L), null, null);

	private static final RelationMember
			RM0 = new OsmRelationMember(0L, "outer", Element.Type.WAY),
			RM1 = new OsmRelationMember(1L, "outer", Element.Type.WAY),
			RM2 = new OsmRelationMember(2L, "inner", Element.Type.WAY),
			RM3 = new OsmRelationMember(3L, null, Element.Type.WAY);

	private static final Relation
			R0 = new OsmRelation(0L, 0, Arrays.asList(RM0, RM1, RM2, RM3), relationArea, null),
			R1 = new OsmRelation(1L, 0, Arrays.asList(RM0, RM1, RM2, RM3), null, null);

	private static final List<Node> nodes = Arrays.asList(N0, N1, N2, N3);
	private static final List<Way> ways = Arrays.asList(W0, W1, W2, W3);

	@Override public Node getNode(long id)
	{
		return nodes.get((int) id);
	}

	@Override public Way getWay(long id)
	{
		return ways.get((int) id);
	}

	public void testCreateForNode()
	{
		ElementGeometry geom = new ElementGeometryCreator(null).create(N0);
		assertEquals(P0, geom.center);
	}

	public void testCreateForEmptyWay()
	{
		ElementGeometry geom = new ElementGeometryCreator(this).create(
				new OsmWay(0L,0, Collections.<Long>emptyList(),null,null)
		);
		assertNull(geom);
	}

	public void testCreateForWayWithDuplicateNodes()
	{
		ElementGeometry geom = new ElementGeometryCreator(this).create(
				new OsmWay(0L,0, Arrays.asList(0L,1L,1L,2L),null,null)
		);
		assertNotNull(geom.polylines);
		assertEquals(P1, geom.center);
	}

	public void testCreateForSimpleAreaWay()
	{
		ElementGeometry geom = new ElementGeometryCreator(this).create(W2);
		assertNotNull(geom.polygons);
		assertEquals(1,geom.polygons.size());
		List<LatLon> polygon = geom.polygons.get(0);

		for(int i=0; i<W2.getNodeIds().size(); ++i)
		{
			LatLon shouldBe = getNode(W2.getNodeIds().get(i)).getPosition();
			assertEquals(shouldBe, polygon.get(i));
		}
	}

	public void testCreateForSimpleNonAreaWay()
	{
		ElementGeometry geom = new ElementGeometryCreator(this).create(W0);
		assertNotNull(geom.polylines);
		assertEquals(1,geom.polylines.size());
		assertEquals(W0.getNodeIds().size(), geom.polylines.get(0).size());

		List<LatLon> polyline = geom.polylines.get(0);

		for(int i=0; i<W0.getNodeIds().size(); ++i)
		{
			LatLon shouldBe = getNode(W0.getNodeIds().get(i)).getPosition();
			assertEquals(shouldBe, polyline.get(i));
		}
	}

	public void testCreateForMultipolygonRelation()
	{
		ElementGeometry geom = new ElementGeometryCreator(this).create(R0);

		assertNotNull(geom.polygons);
		assertEquals(2, geom.polygons.size());
	}

	public void testCreateForPolylineRelation()
	{
		ElementGeometry geom = new ElementGeometryCreator(this).create(R1);

		assertNotNull(geom.polylines);
		assertEquals(3, geom.polylines.size());
	}
}