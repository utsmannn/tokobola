package com.utsman.tokobola.common.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChipTabs(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    itemCount: Int,
    tabEnable: Boolean = true,
    onTabClick: (Int) -> Unit = {},
    tabContent: @Composable ColumnScope.(index: Int, selectedTextColor: Color) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier.then(
            Modifier.fillMaxWidth()
        ),
        backgroundColor = Color.Transparent,
        edgePadding = 12.dp,
        indicator = {},
        divider = {}
    ) {
        (0 until itemCount-1).forEachIndexed { index, _ ->
            val isSelected by remember {
                derivedStateOf {
                    index == selectedTabIndex
                }
            }

            val selectedTabColor = animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colors.primary else Color.White
            )

            val selectedTextColor = animateColorAsState(
                targetValue = if (isSelected) Color.White else Color.Black
            )

            Tab(
                selected = isSelected,
                onClick = {
                    onTabClick.invoke(index)
                },
                enabled = tabEnable,
                modifier = Modifier
                    .padding(
                        vertical = 12.dp,
                        horizontal = 6.dp
                    )
                    .shadow(4.dp, clip = false, shape = RoundedCornerShape(18.dp))
                    .clip(RoundedCornerShape(18.dp))
                    .background(color = selectedTabColor.value)
            ) {
                tabContent.invoke(this, index, selectedTextColor.value)
            }
        }
    }
}