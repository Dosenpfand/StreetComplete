package de.westnordost.streetcomplete.quests.religion

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.SimpleOverpassQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.download.OverpassMapDataAndGeometryDao

class AddReligionToCemetery(o: OverpassMapDataAndGeometryDao) : SimpleOverpassQuestType<String>(o) {

    override val tagFilters = "nodes, ways, relations with landuse = cemetery and !religion"
    override val commitMessage = "Add religion for cemetery"
    override val icon = R.drawable.ic_quest_religion

    override fun getTitle(tags: Map<String, String>): Int {
        val hasName = tags.containsKey("name")
        return if (hasName)
            R.string.quest_religion_for_cemetery_name_title
        else
            R.string.quest_religion_for_cemetery_title
    }

    override fun createForm() = AddReligionForm()

    override fun applyAnswerTo(answer: String, changes: StringMapChangesBuilder) {
        changes.add("religion", answer)
    }
}
