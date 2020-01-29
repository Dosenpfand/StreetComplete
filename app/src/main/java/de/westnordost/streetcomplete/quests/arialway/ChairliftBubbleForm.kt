package de.westnordost.streetcomplete.quests.arialway

import de.westnordost.streetcomplete.quests.AYesNoQuestAnswerFragment

class ChairliftBubbleForm : AYesNoQuestAnswerFragment<String>() {

    override fun onClick(answer: Boolean) {
        applyAnswer(if(answer) "yes" else "no")
    }
}
