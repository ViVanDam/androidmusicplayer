package com.example.adamandroid.ui.theme.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adamandroid.data.ScannedSong
import com.example.adamandroid.ui.theme.components.ScannedSongItem
import com.example.adamandroid.ui.theme.scanmusic.ScanMusicViewModel

@Composable
fun ScanMusicScreen(
    viewModel: ScanMusicViewModel,
    onAddSongs: (List<ScannedSong>) -> Unit,
    onBackClick: () -> Unit
) {

    // =========================
    // PERMISSION
    // =========================

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->

        if (granted) {
            viewModel.scanMusic()
        }
    }

    LaunchedEffect(Unit) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            permissionLauncher.launch(
                Manifest.permission.READ_MEDIA_AUDIO
            )

        } else {

            permissionLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    // =========================
    // STATE
    // =========================

    val songs by viewModel.songs.collectAsState()

    var selectedSongIds by remember {
        mutableStateOf<Set<Long>>(emptySet())
    }

    val selectedSongs = songs.filter {
        it.id in selectedSongIds
    }

    val allSelected =
        songs.isNotEmpty() &&
                selectedSongIds.size == songs.size

    // =========================
    // SCREEN
    // =========================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF0D111C)
            )
    ) {

        // =========================
        // HEADER
        // =========================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 12.dp,
                    end = 20.dp,
                    top = 50.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = "Thêm nhạc",
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    viewModel.scanMusic()
                }
            ) {

                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Scan music",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // =========================
        // SELECT ALL
        // =========================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = allSelected,
                onCheckedChange = { checked ->

                    selectedSongIds =
                        if (checked) {

                            songs
                                .map { it.id }
                                .toSet()

                        } else {

                            emptySet()
                        }
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF2196F3),
                    uncheckedColor = Color(0xFF9CA3C7),
                    checkmarkColor = Color.White
                )
            )

            Text(
                text = if (selectedSongs.isEmpty()) {
                    "Chọn tất cả"
                } else {
                    "Đã chọn ${selectedSongs.size} bài"
                },
                color = Color.White,
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${songs.size} bài",
                color = Color(0xFF9CA3C7),
                fontSize = 14.sp
            )
        }

        // =========================
        // MUSIC LIST
        // =========================

        Box(
            modifier = Modifier.weight(1f)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 20.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                items(
                    items = songs,
                    key = { it.id }
                ) { song ->

                    ScannedSongItem(
                        song = song,
                        selected = song.id in selectedSongIds,
                        onClick = {

                            selectedSongIds =
                                if (song.id in selectedSongIds) {

                                    selectedSongIds - song.id

                                } else {

                                    selectedSongIds + song.id
                                }
                        }
                    )
                }

                // Khoảng trống cuối danh sách
                item {

                    Spacer(
                        modifier = Modifier.height(100.dp)
                    )
                }
            }
        }

        // =========================
        // ADD BUTTON
        // =========================

        Button(
            onClick = {
                onAddSongs(selectedSongs)
            },
            enabled = selectedSongs.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
                .height(52.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3),
                disabledContainerColor = Color(0xFF252B3A),
                disabledContentColor = Color(0xFF6B7280)
            )
        ) {

            Text(
                text = if (selectedSongs.isEmpty()) {
                    "Thêm vào app"
                } else {
                    "Thêm vào app (${selectedSongs.size})"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
