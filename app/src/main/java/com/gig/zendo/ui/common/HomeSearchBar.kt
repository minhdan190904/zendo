package com.gig.zendo.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.gig.zendo.R
import com.gig.zendo.ui.theme.DarkGreen
import com.gig.zendo.utils.Dimens


@Composable
fun HomeSearchBar(
    modifier: Modifier = Modifier,
    onFilterClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .padding(horizontal = Dimens.dp16)
            .fillMaxWidth(),
    ) {
        Box(
            modifier = modifier
                .size(Dimens.dp40)
                .shadow(elevation = Dimens.dp3, shape = CircleShape)
                .background(color = Color.White, shape = CircleShape)
                .clickable { onFilterClick() },
        ) {
            Icon(
                modifier = modifier
                    .fillMaxSize()
                    .padding(Dimens.dp12),
                painter = painterResource(id = R.drawable.ic_filter),
                tint = DarkGreen,
                contentDescription = "Filter",
            )
        }
        Spacer(modifier = modifier.width(Dimens.dp24))
        SearchBar(
            hint = "Search",
            cornerShape = RoundedCornerShape(Dimens.dp20),
        )
    }
}

@Composable
@Preview
private fun HomeSearchBarPreview() {
    HomeSearchBar(
        modifier = Modifier
    )
}