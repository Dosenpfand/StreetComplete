package de.westnordost.streetcomplete.quests.surveillance_camera_type

import android.os.Bundle

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.quests.AImageListQuestAnswerFragment
import de.westnordost.streetcomplete.view.Item

class AddSurveillanceCameraTypeForm : AImageListQuestAnswerFragment<String,String>() {

    override val items get() = listOf(
            Item("fixed",   R.drawable.ic_surveillance_camera_type_fixed,   R.string.quest_surveillance_camera_type_fixed),
            Item("panning", R.drawable.ic_surveillance_camera_type_panning, R.string.quest_surveillance_camera_type_panning),
            Item("dome",    R.drawable.ic_surveillance_camera_type_dome,    R.string.quest_surveillance_camera_type_dome)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageSelector.cellLayoutId = R.layout.cell_icon_select_with_label_below
    }

    override fun onClickOk(selectedItems: List<String>) {
        applyAnswer(selectedItems.single())
    }
}
