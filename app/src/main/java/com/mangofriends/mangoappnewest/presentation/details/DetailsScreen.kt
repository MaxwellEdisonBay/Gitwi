package com.mangofriends.mangoappnewest.presentation.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mangofriends.mangoappnewest.R
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile
import com.mangofriends.mangoappnewest.presentation.ui.theme.LightPink

@ExperimentalCoilApi
@Composable
fun DetailsScreen(
    profile: DTOUserProfile, navController: NavController,
    viewModel: DetailsViewModel = hiltViewModel()
) {

    val rowTitles = stringArrayResource(id = R.array.details_row_titles)
    val rowValues = listOf(
        profile.gender,
        profile.age.toString(),
        profile.location,
        profile.occupation,
        profile.bio
    )
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = rememberImagePainter(data = profile.profile_image_urls[0].url),
            contentDescription = "",
            modifier = Modifier
                .requiredHeight(400.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = profile.name,
            style = MaterialTheme.typography.h1,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)

        )
        repeat(rowTitles.size) {
            DetailsRow(
                title = rowTitles[it],
                imageVector = Constants.DETAIL_ROWS_ICONS[it],
                value = rowValues[it]
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


    }

}

@Composable
fun DetailsRow(title: String, imageVector: ImageVector, value: String) {
    if (value.isNotEmpty())
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h2,

                    )
                Icon(imageVector, "", modifier = Modifier.size(25.dp), tint = LightPink)

            }
            Divider(color = Color.LightGray, thickness = 1.dp)

            Text(
                text = value,
                style = MaterialTheme.typography.body1,
            )
        }
}