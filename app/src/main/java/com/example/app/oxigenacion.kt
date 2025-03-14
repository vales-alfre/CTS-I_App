import android.graphics.Paint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OxygenScreen(navController: NavController) {
    val oxygenData = remember { mutableStateListOf<Int>() }
    val showSimulation = remember { mutableStateOf(true) }
    val context = LocalContext.current

    if (oxygenData.isEmpty()) {
        repeat(5) { oxygenData.add(Random.nextInt(85, 100)) }
    }

    LaunchedEffect(showSimulation.value) {
        while (showSimulation.value) {
            delay(5000)
            if (oxygenData.size >= 5) oxygenData.removeAt(0)
            oxygenData.add(Random.nextInt(85, 100))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Oxigenación Sanguínea", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Retroceder",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues))
                {
                    OxygenStatusCard(
                        currentOxygen = oxygenData.lastOrNull() ?: 98,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OxygenChart(
                        data = oxygenData,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .padding(horizontal = 16.dp)
                    )

                    TimeLabels(modifier = Modifier.padding(16.dp))
                }
    }
}

@Composable
private fun OxygenStatusCard(currentOxygen: Int, modifier: Modifier = Modifier) {
    val statusColor = getOxygenStatusColor(currentOxygen)
    val animatedElevation by animateDpAsState(
        targetValue = if (statusColor == MaterialTheme.colorScheme.error) 12.dp else 6.dp,
        label = "elevationAnimation"
    )

    Card(
        modifier = modifier
            .shadow(
                elevation = animatedElevation,
                shape = RoundedCornerShape(20.dp),
                spotColor = statusColor.copy(alpha = 0.2f)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            statusColor.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "OXIGENACIÓN ACTUAL",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "$currentOxygen%",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            color = statusColor.copy(alpha = 0.15f),
                            shape = CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = statusColor.copy(alpha = 0.4f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MonitorHeart,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
                Text(
                    text = getOxygenStatusText(currentOxygen).uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = statusColor,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun OxygenChart(data: List<Int>, modifier: Modifier = Modifier) {
    val maxY = (data.maxOrNull() ?: 100) + 2.0
    val minY = (data.minOrNull() ?: 85) - 2.0
    val lineColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val chartPadding = 20.dp.toPx()

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFE8F5E9).copy(alpha = 0.4f),
                    Color(0xFFF1F8E9).copy(alpha = 0.2f)
                )
            ),
            size = size
        )

        val textPaint = Paint().apply {
            color = textColor.hashCode()
            textSize = 30f
            textAlign = Paint.Align.LEFT
            isAntiAlias = true
        }

        val steps = 4
        val stepValue = (maxY - minY) / steps
        repeat(steps + 1) { i ->
            val value = (minY + (stepValue * i)).roundToInt()
            val yPosition = canvasHeight - ((value - minY).toFloat() / (maxY - minY)) * canvasHeight

            drawLine(
                start = Offset(60f, yPosition.toFloat()),
                end = Offset(canvasWidth - chartPadding, yPosition.toFloat()),
                color = gridColor.copy(alpha = 0.3f),
                strokeWidth = 1.dp.toPx()
            )

            drawContext.canvas.nativeCanvas.drawText(
                "$value%",
                40f,
                (yPosition + 15f).toFloat(),
                textPaint
            )
        }

        val gridLines = 4
        repeat(gridLines + 1) { i ->
            val yPos = canvasHeight * (i.toFloat() / gridLines)
            drawLine(
                color = gridColor,
                start = Offset(0f, yPos),
                end = Offset(canvasWidth - chartPadding, yPos),
                strokeWidth = 1.dp.toPx()
            )
        }

        val dataPoints = data.mapIndexed { index, value ->
            Offset(
                x = ((canvasWidth - chartPadding) / (data.size - 1)) * index,
                y = ((canvasHeight - ((value - minY).toFloat() / (maxY - minY)) * canvasHeight).toFloat())
            )
        }

        val path = Path().apply {
            dataPoints.forEachIndexed { index, offset ->
                when (index) {
                    0 -> moveTo(offset.x, offset.y)
                    else -> {
                        val previous = dataPoints[index - 1]
                        val control1 = Offset(previous.x + (offset.x - previous.x) * 0.5f, previous.y)
                        val control2 = Offset(previous.x + (offset.x - previous.x) * 0.5f, offset.y)
                        cubicTo(
                            control1.x, control1.y,
                            control2.x, control2.y,
                            offset.x, offset.y
                        )
                    }
                }
            }
        }

        drawPath(
            path = path,
            color = lineColor.copy(alpha = 0.2f),
            style = Stroke(
                width = 12f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(
                width = 4f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        dataPoints.forEach { point ->
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        lineColor.copy(alpha = 0.4f),
                        Color.Transparent
                    ),
                    center = point,
                    radius = 20f
                )
            )
            drawCircle(
                color = Color(0xFFE8F5E9).copy(alpha = 0.4f),
                radius = 6f,
                center = point
            )
        }
    }
}

private fun getOxygenStatusText(oxygen: Int): String = when {
    oxygen < 90 -> "Crítico"
    oxygen in 90..94 -> "Bajo"
    else -> "Normal"
}

@Composable
private fun getOxygenStatusColor(oxygen: Int): Color = when {
    oxygen < 90 -> MaterialTheme.colorScheme.error
    oxygen in 90..94 -> MaterialTheme.colorScheme.tertiary
    else -> MaterialTheme.colorScheme.primary
}

// TimeLabels se mantiene igual que en ejemplos anteriores
@Composable
private fun TimeLabels(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(5) { index ->
            Text(
                text = "M${index + 1}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}