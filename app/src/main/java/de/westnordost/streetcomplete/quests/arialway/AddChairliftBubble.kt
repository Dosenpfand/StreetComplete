package de.westnordost.streetcomplete.quests.arialway

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.SimpleOverpassQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.download.OverpassMapDataAndGeometryDao

class AddChairliftBubble(o: OverpassMapDataAndGeometryDao) : SimpleOverpassQuestType<String>(o) {

    override val tagFilters = "ways with aerialway=chair_lift and !aerialway:bubble"
    override val commitMessage = "Add wether chair lift carriages have bubbles."
    override val icon = R.drawable.ic_quest_wheelchair_outside

    override fun getTitle(tags: Map<String, String>): Int {
        val hasName = tags.containsKey("name")
        return if (hasName)
            R.string.quest_chairlift_bubble_name_title
        else
            R.string.quest_chairlift_bubble_title
    }

    override fun createForm() = ChairliftBubbleForm()

    override fun applyAnswerTo(answer: String, changes: StringMapChangesBuilder) {
        changes.add("aerialway:bubble", answer)
    }
}
