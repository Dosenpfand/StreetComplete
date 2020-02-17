package de.westnordost.streetcomplete.quests.surveillance_camera_type

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.SimpleOverpassQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.download.OverpassMapDataAndGeometryDao

class AddSurveillanceCameraType(o: OverpassMapDataAndGeometryDao) : SimpleOverpassQuestType<String>(o) {

    override val icon = R.drawable.ic_quest_surveillance

    override val tagFilters =
            "nodes with man_made = surveillance and surveillance = public and surveillance:type = camera and !camera:type"

    override fun getTitle(tags: Map<String, String>) = R.string.quest_surveillance_camera_type_title

    override fun createForm() = AddSurveillanceCameraTypeForm()

    override fun applyAnswerTo(answer: String, changes: StringMapChangesBuilder) {
        changes.add("camera:type", answer)
    }

    override val commitMessage = "Add surveillance camera type"

}
