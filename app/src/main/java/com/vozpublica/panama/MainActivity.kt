package com.vozpublica.panama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFF11151C)
private val Panel = Color(0xFF171C24)
private val PanelRaised = Color(0xFF1C222C)
private val Border = Color(0xFF2B323D)
private val PrimaryText = Color(0xFFF0F2F6)
private val MutedText = Color(0xFF9AA4B2)
private val Blue = Color(0xFF91BAFF)
private val Gold = Color(0xFFF6BD59)
private val AmberPanel = Color(0xFF282319)

private data class Review(
    val author: String,
    val initials: String,
    val rating: Int,
    val date: String,
    val text: String,
    val response: String? = null,
)

private data class Institution(
    val name: String,
    val province: String,
    val city: String,
    val address: String,
    val category: String,
    val symbol: String,
    val latestReviewAt: String,
    val reviews: List<Review>,
) {
    val rating: Float
        get() = reviews.map { it.rating }.average().toFloat()
}

private val provinces = listOf(
    "Bocas del Toro",
    "Chiriquí",
    "Coclé",
    "Colón",
    "Darién",
    "Herrera",
    "Los Santos",
    "Panamá",
    "Panamá Oeste",
    "Veraguas",
)

private val sampleInstitutions = listOf(
    Institution(
        name = "Policlínica Dr. Manuel Paulino Ocaña",
        province = "Coclé",
        city = "Penonomé",
        address = "FJRR+873, Penonomé, Provincia de Coclé",
        category = "Salud",
        symbol = "✚",
        latestReviewAt = "2025-05-22",
        reviews = listOf(
            Review(
                "Mickel Ortega", "MO", 5, "Hace 3 semanas",
                "Las instalaciones son cómodas y limpias. Haría falta un poco más de señalización para quienes llegan por primera vez desde otros distritos.",
            ),
            Review(
                "Ana Lucía González", "AG", 3, "Hace 4 meses",
                "La atención fue amable cuando por fin me llamaron. Recomiendo llegar temprano porque la espera puede ser larga.",
                "Gracias por compartir tu experiencia. Estamos trabajando para mejorar la orientación y los tiempos de atención.",
            ),
            Review(
                "Carlos Méndez", "CM", 4, "Hace 7 meses",
                "Buena atención en farmacia y espacios renovados. Conviene confirmar el horario de la especialidad antes de viajar.",
            ),
        ),
    ),
    Institution(
        name = "Hospital Regional Dr. Rafael Hernández",
        province = "Chiriquí",
        city = "David",
        address = "Vía Panamericana, David, Provincia de Chiriquí",
        category = "Salud",
        symbol = "✚",
        latestReviewAt = "2025-06-08",
        reviews = listOf(
            Review(
                "María Ríos", "MR", 4, "Hace 2 semanas",
                "El personal de urgencias fue atento y explicó los pasos con claridad. La sala estaba concurrida, pero nos mantuvieron informados.",
            ),
            Review(
                "José Batista", "JB", 2, "Hace 3 meses",
                "Hay buenos profesionales, aunque encontrar el área correcta puede ser complicado si no conoces el hospital.",
            ),
            Review(
                "Elena Guerra", "EG", 5, "Hace 6 meses",
                "La atención de enfermería fue muy humana y paciente. Agradezco que respondieran todas mis preguntas.",
                "Nos alegra conocer tu experiencia. Compartiremos tus palabras con el equipo de enfermería.",
            ),
        ),
    ),
    Institution(
        name = "Municipio de Panamá",
        province = "Panamá",
        city = "Ciudad de Panamá",
        address = "Edificio Hatillo, Avenida Justo Arosemena, Panamá",
        category = "Gobierno local",
        symbol = "⌂",
        latestReviewAt = "2025-06-11",
        reviews = listOf(
            Review(
                "Daniela Vega", "DV", 4, "Hace 1 semana",
                "Pude hacer el trámite en una sola visita. La información en recepción fue clara, aunque sería útil tener más asientos.",
            ),
            Review(
                "Roberto Castillo", "RC", 2, "Hace 2 meses",
                "El proceso fue más lento de lo esperado y no todos los requisitos estaban claros en línea. El personal del módulo sí intentó ayudar.",
            ),
            Review(
                "Sofía Herrera", "SH", 5, "Hace 8 meses",
                "Me orientaron muy bien para actualizar mis datos. El lugar estaba ordenado y encontré fácilmente el departamento que necesitaba.",
                "Gracias por tus comentarios. Nos ayudan a seguir mejorando la atención a la ciudadanía.",
            ),
        ),
    ),
    Institution(
        name = "Registro Público — Oficina de La Chorrera",
        province = "Panamá Oeste",
        city = "La Chorrera",
        address = "Avenida de las Américas, La Chorrera, Panamá Oeste",
        category = "Trámites",
        symbol = "▤",
        latestReviewAt = "2025-06-04",
        reviews = listOf(
            Review(
                "Luis Arosemena", "LA", 5, "Hace 2 semanas",
                "Me atendieron rápido y me indicaron exactamente qué documentos hacían falta. Buen servicio en ventanilla.",
            ),
            Review(
                "Patricia Cedeño", "PC", 4, "Hace 4 meses",
                "Es una oficina pequeña, pero está bien organizada. Recomiendo revisar los horarios de atención antes de ir.",
            ),
            Review(
                "Marcos Díaz", "MD", 3, "Hace 10 meses",
                "Resolví mi gestión, aunque tuve que esperar bastante por la cantidad de personas.",
            ),
        ),
    ),
    Institution(
        name = "Hospital Dr. Luis 'Chicho' Fábrega",
        province = "Veraguas",
        city = "Santiago",
        address = "Vía Interamericana, Santiago, Provincia de Veraguas",
        category = "Salud",
        symbol = "✚",
        latestReviewAt = "2025-05-29",
        reviews = listOf(
            Review(
                "Gloria Pitti", "GP", 5, "Hace 3 semanas",
                "El equipo nos trató con mucho respeto. La orientación en consulta externa fue sencilla y encontramos el área sin problema.",
            ),
            Review(
                "Andrés Muñoz", "AM", 3, "Hace 3 meses",
                "La atención médica fue buena. El estacionamiento se llena temprano, así que es mejor llegar con tiempo.",
            ),
            Review(
                "Beatriz Navarro", "BN", 4, "Hace 6 meses",
                "Me explicaron claramente el tratamiento y las indicaciones para continuar en casa.",
                "Agradecemos que nos cuentes cómo fue tu visita. Tu reconocimiento motiva a nuestro equipo.",
            ),
        ),
    ),
    Institution(
        name = "Autoridad Nacional de Aduanas — Colón",
        province = "Colón",
        city = "Colón",
        address = "Zona Libre de Colón, Provincia de Colón",
        category = "Gobierno nacional",
        symbol = "▤",
        latestReviewAt = "2025-04-27",
        reviews = listOf(
            Review(
                "Kevin Thomas", "KT", 4, "Hace 2 meses",
                "La información para completar el trámite fue precisa. Sugiero llevar copias extra de los documentos.",
            ),
            Review(
                "Rosa Lee", "RL", 3, "Hace 5 meses",
                "Logré resolver mi consulta, pero tuve que preguntar en varios lugares para encontrar la ventanilla indicada.",
            ),
            Review(
                "Edwin Bailey", "EB", 4, "Hace 9 meses",
                "Personal profesional y dispuesto a orientar sobre los pasos del proceso.",
            ),
        ),
    ),
    Institution(
        name = "Dirección Regional del MIDA en Herrera",
        province = "Herrera",
        city = "Chitré",
        address = "Carretera Nacional, Chitré, Provincia de Herrera",
        category = "Agricultura",
        symbol = "⌂",
        latestReviewAt = "2025-05-12",
        reviews = listOf(
            Review(
                "Pedro Saavedra", "PS", 5, "Hace 1 mes",
                "Recibí asesoría práctica para mi finca y me explicaron dónde entregar los formularios.",
            ),
            Review(
                "Carmen Solís", "CS", 4, "Hace 4 meses",
                "Buena atención. Sería conveniente que publicaran los horarios de las visitas técnicas con más anticipación.",
            ),
            Review(
                "Nicolás Ríos", "NR", 4, "Hace 7 meses",
                "Me orientaron sobre los programas disponibles para pequeños productores.",
            ),
        ),
    ),
    Institution(
        name = "Municipio de Las Tablas",
        province = "Los Santos",
        city = "Las Tablas",
        address = "Avenida Belisario Porras, Las Tablas, Los Santos",
        category = "Gobierno local",
        symbol = "⌂",
        latestReviewAt = "2025-06-01",
        reviews = listOf(
            Review(
                "Yadira González", "YG", 5, "Hace 2 semanas",
                "Personal amable y el trámite de permisos fue más sencillo de lo que esperaba.",
            ),
            Review(
                "Ricardo Villarreal", "RV", 3, "Hace 3 meses",
                "El edificio es fácil de ubicar. Hay que verificar qué oficina atiende cada gestión.",
            ),
            Review(
                "Estela Díaz", "ED", 4, "Hace 8 meses",
                "Me dieron información útil sobre los servicios municipales y los horarios.",
            ),
        ),
    ),
    Institution(
        name = "Centro de Salud de Metetí",
        province = "Darién",
        city = "Metetí",
        address = "Carretera Panamericana, Metetí, Provincia de Darién",
        category = "Salud",
        symbol = "✚",
        latestReviewAt = "2025-05-17",
        reviews = listOf(
            Review(
                "Milagros Mena", "MM", 5, "Hace 1 mes",
                "La doctora y el personal de enfermería fueron muy atentos. Es importante llevar el carné de salud.",
            ),
            Review(
                "Samuel Córdoba", "SC", 3, "Hace 4 meses",
                "La atención fue correcta, aunque en algunos días no hay todos los medicamentos disponibles.",
            ),
            Review(
                "Nora Gutiérrez", "NG", 4, "Hace 9 meses",
                "Me indicaron cómo continuar la atención en otra instalación de la región.",
            ),
        ),
    ),
    Institution(
        name = "Tribunal Electoral — Oficina de Changuinola",
        province = "Bocas del Toro",
        city = "Changuinola",
        address = "Avenida 17 de Abril, Changuinola, Bocas del Toro",
        category = "Trámites",
        symbol = "▤",
        latestReviewAt = "2025-06-06",
        reviews = listOf(
            Review(
                "Iris Brown", "IB", 5, "Hace 2 semanas",
                "Actualicé mis datos rápido y el personal verificó todos mis documentos con paciencia.",
            ),
            Review(
                "Miguel Salas", "MS", 4, "Hace 4 meses",
                "Buena orientación y ubicación céntrica. El lugar estaba limpio y organizado.",
            ),
            Review(
                "Diana López", "DL", 4, "Hace 8 meses",
                "La fila avanzó bien y pude terminar el trámite durante la mañana.",
            ),
        ),
    ),
)

private enum class SortMode(val label: String) {
    Relevant("Más relevantes"),
    Recent("Más recientes"),
    Highest("Mejor calificación"),
    Lowest("Menor calificación"),
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(17, 21, 28)
        window.navigationBarColor = android.graphics.Color.rgb(17, 21, 28)
        setContent {
            VozPublicaTheme {
                VozPublicaApp()
            }
        }
    }
}

@Composable
private fun VozPublicaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Blue,
            onPrimary = Background,
            background = Background,
            surface = Panel,
            onSurface = PrimaryText,
            secondary = Gold,
        ),
        content = content,
    )
}

@Composable
private fun VozPublicaApp() {
    var selectedProvince by rememberSaveable { mutableStateOf("") }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var reviewQuery by rememberSaveable { mutableStateOf("") }
    var selectedInstitutionName by rememberSaveable {
        mutableStateOf("Policlínica Dr. Manuel Paulino Ocaña")
    }
    var sortModeName by rememberSaveable { mutableStateOf(SortMode.Relevant.name) }
    var sortMenuExpanded by remember { mutableStateOf(false) }
    val sortMode = SortMode.valueOf(sortModeName)

    val filteredInstitutions = remember(selectedProvince, searchQuery, sortMode) {
        sampleInstitutions
            .filter { selectedProvince.isBlank() || it.province == selectedProvince }
            .filter {
                searchQuery.isBlank() ||
                    it.name.contains(searchQuery, ignoreCase = true) ||
                    it.city.contains(searchQuery, ignoreCase = true) ||
                    it.category.contains(searchQuery, ignoreCase = true)
            }
            .sortedWith(
                when (sortMode) {
                    SortMode.Relevant -> compareByDescending<Institution> { it.reviews.size }
                    SortMode.Recent -> compareByDescending<Institution> { it.latestReviewAt }
                    SortMode.Highest -> compareByDescending<Institution> { it.rating }
                    SortMode.Lowest -> compareBy<Institution> { it.rating }
                },
            )
    }
    val selectedInstitution = filteredInstitutions.firstOrNull {
        it.name == selectedInstitutionName
    } ?: filteredInstitutions.firstOrNull()
    val filteredReviews = remember(selectedInstitution, reviewQuery, sortMode) {
        selectedInstitution?.reviews
            .orEmpty()
            .filter {
                reviewQuery.isBlank() ||
                    it.text.contains(reviewQuery, ignoreCase = true) ||
                    it.author.contains(reviewQuery, ignoreCase = true)
            }
            .let { reviews ->
                when (sortMode) {
                    SortMode.Relevant -> reviews
                    SortMode.Recent -> reviews
                    SortMode.Highest -> reviews.sortedByDescending { it.rating }
                    SortMode.Lowest -> reviews.sortedBy { it.rating }
                }
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            AppHeader()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                Hero()
                DemoNotice()
                SectionLabel("EXPLORAR POR PROVINCIA")
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChip(
                        selected = selectedProvince.isBlank(),
                        onClick = { selectedProvince = "" },
                        label = { Text("Todas") },
                        colors = provinceChipColors(),
                    )
                    provinces.forEach { province ->
                        FilterChip(
                            selected = selectedProvince == province,
                            onClick = {
                                selectedProvince = if (selectedProvince == province) "" else province
                            },
                            label = { Text(province) },
                            colors = provinceChipColors(),
                        )
                    }
                }
                Spacer(Modifier.height(22.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        SectionLabel("DIRECTORIO CIUDADANO")
                        Spacer(Modifier.height(5.dp))
                        Text(
                            text = if (selectedProvince.isBlank()) {
                                "Instituciones públicas"
                            } else {
                                "Instituciones en $selectedProvince"
                            },
                            color = PrimaryText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Text(
                        text = "${filteredInstitutions.size}",
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(PanelRaised)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        color = MutedText,
                        fontSize = 12.sp,
                    )
                }
                Spacer(Modifier.height(7.dp))
                Text(
                    text = "Opiniones de muestra sobre servicios e instituciones públicas.",
                    color = MutedText,
                    fontSize = 12.sp,
                )
                Spacer(Modifier.height(14.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Buscar institución o distrito") },
                    leadingIcon = { Text("⌕", color = MutedText, fontSize = 21.sp) },
                    colors = searchFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                )
                Spacer(Modifier.height(8.dp))
                Box {
                    Button(
                        onClick = { sortMenuExpanded = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PanelRaised,
                            contentColor = PrimaryText,
                        ),
                        border = BorderStroke(1.dp, Border),
                    ) {
                        Text("Ordenar: ${sortMode.label}", fontSize = 12.sp)
                    }
                    DropdownMenu(
                        expanded = sortMenuExpanded,
                        onDismissRequest = { sortMenuExpanded = false },
                        containerColor = PanelRaised,
                    ) {
                        SortMode.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.label, color = PrimaryText, fontSize = 13.sp) },
                                onClick = {
                                    sortModeName = option.name
                                    sortMenuExpanded = false
                                },
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                if (filteredInstitutions.isEmpty()) {
                    EmptyState(
                        title = "No encontramos instituciones",
                        description = "Prueba otra búsqueda o selecciona una provincia diferente.",
                    )
                } else {
                    filteredInstitutions.forEach { institution ->
                        InstitutionCard(
                            institution = institution,
                            selected = institution.name == selectedInstitution?.name,
                            onClick = {
                                selectedInstitutionName = institution.name
                                reviewQuery = ""
                            },
                        )
                        Spacer(Modifier.height(9.dp))
                    }
                }

                Spacer(Modifier.height(12.dp))
                selectedInstitution?.let { institution ->
                    InstitutionDetail(
                        institution = institution,
                        reviews = filteredReviews,
                        reviewQuery = reviewQuery,
                        onReviewQueryChange = { reviewQuery = it },
                    )
                }
                Spacer(Modifier.height(20.dp))
                AboutCard()
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "PROTOTIPO CIUDADANO · PANAMÁ",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    color = MutedText,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                )
            }
        }
    }
}

@Composable
private fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF202C3E)),
                contentAlignment = Alignment.Center,
            ) {
                Text("◉", color = Blue, fontSize = 18.sp)
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = "voz",
                color = PrimaryText,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
            )
            Text("pública", color = MutedText, fontSize = 19.sp)
        }
        Text(
            text = "●  DEMO",
            modifier = Modifier
                .clip(CircleShape)
                .background(AmberPanel)
                .padding(horizontal = 11.dp, vertical = 7.dp),
            color = Color(0xFFE6CB91),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
        )
    }
}

@Composable
private fun Hero() {
    Column(modifier = Modifier.padding(top = 22.dp, bottom = 18.dp)) {
        SectionLabel("PANAMÁ · SERVICIOS PÚBLICOS")
        Spacer(Modifier.height(12.dp))
        Text(
            text = "La voz de la gente,\nen cada provincia.",
            color = PrimaryText,
            fontSize = 34.sp,
            lineHeight = 39.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-1).sp,
        )
        Spacer(Modifier.height(9.dp))
        Text(
            text = "Explora experiencias ciudadanas sobre instituciones públicas de todo Panamá.",
            color = MutedText,
            fontSize = 13.sp,
            lineHeight = 19.sp,
        )
    }
}

@Composable
private fun DemoNotice() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(AmberPanel)
            .border(1.dp, Color(0xFF55472D), RoundedCornerShape(10.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text("ⓘ", color = Gold, fontSize = 16.sp)
        Spacer(Modifier.width(9.dp))
        Text(
            text = "Datos de demostración. Estas opiniones son ejemplos ficticios; no son reseñas reales de Google.",
            color = Color(0xFFE0D0AC),
            fontSize = 11.sp,
            lineHeight = 16.sp,
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = Color(0xFF8C9AAD),
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.3.sp,
    )
}

@Composable
private fun provinceChipColors() = FilterChipDefaults.filterChipColors(
    selectedContainerColor = Color(0xFF314D76),
    selectedLabelColor = Color(0xFFE2EDFF),
    containerColor = Panel,
    labelColor = MutedText,
)

@Composable
private fun searchFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Blue,
    unfocusedBorderColor = Border,
    focusedTextColor = PrimaryText,
    unfocusedTextColor = PrimaryText,
    focusedLabelColor = Blue,
    unfocusedLabelColor = MutedText,
    cursorColor = Blue,
)

@Composable
private fun InstitutionCard(
    institution: Institution,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val cardBorder = if (selected) Color(0xFF5279B0) else Border
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Color(0xFF202B3A) else Panel,
        ),
        border = BorderStroke(1.dp, cardBorder),
    ) {
        Row(
            modifier = Modifier.padding(13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(Color(0xFF263448)),
                contentAlignment = Alignment.Center,
            ) {
                Text(institution.symbol, color = Blue, fontSize = 17.sp)
            }
            Spacer(Modifier.width(11.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = institution.name,
                    color = PrimaryText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = "${institution.city} · ${institution.province}",
                    color = MutedText,
                    fontSize = 10.sp,
                )
                Spacer(Modifier.height(7.dp))
                RatingLine(institution.rating)
            }
            Text(
                text = "${institution.reviews.size} ejemplos",
                color = MutedText,
                fontSize = 9.sp,
            )
        }
    }
}

@Composable
private fun RatingLine(rating: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = String.format("%.1f", rating),
            color = PrimaryText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "★".repeat(rating.toInt().coerceIn(0, 5)) +
                "☆".repeat((5 - rating.toInt()).coerceIn(0, 5)),
            color = Gold,
            fontSize = 11.sp,
            letterSpacing = (-1).sp,
        )
    }
}

@Composable
private fun InstitutionDetail(
    institution: Institution,
    reviews: List<Review>,
    reviewQuery: String,
    onReviewQueryChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 9.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Panel)
            .border(1.dp, Border, RoundedCornerShape(12.dp))
            .padding(15.dp),
    ) {
        Text(
            text = "${institution.category.uppercase()} · ${institution.city}, ${institution.province}",
            color = Blue,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.7.sp,
        )
        Spacer(Modifier.height(7.dp))
        Text(
            text = institution.name,
            color = PrimaryText,
            fontSize = 18.sp,
            lineHeight = 23.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = "${institution.address} · Panamá",
            color = MutedText,
            fontSize = 10.sp,
            lineHeight = 15.sp,
        )
        Spacer(Modifier.height(11.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = String.format("%.1f", institution.rating),
                color = PrimaryText,
                fontSize = 27.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.width(9.dp))
            Column {
                RatingLine(institution.rating)
                Text(
                    text = "${institution.reviews.size} opiniones de ejemplo",
                    color = MutedText,
                    fontSize = 9.sp,
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = reviewQuery,
            onValueChange = onReviewQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Buscar en las opiniones") },
            colors = searchFieldColors(),
            shape = RoundedCornerShape(9.dp),
        )
        Spacer(Modifier.height(8.dp))
        if (reviews.isEmpty()) {
            Text(
                text = "No hay opiniones de muestra que coincidan con la búsqueda.",
                modifier = Modifier.padding(vertical = 15.dp),
                color = MutedText,
                fontSize = 11.sp,
            )
        } else {
            reviews.forEachIndexed { index, review ->
                if (index > 0) {
                    Spacer(Modifier.height(13.dp))
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Border),
                    )
                    Spacer(Modifier.height(13.dp))
                }
                ReviewCard(review)
            }
        }
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF344761)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = review.initials,
                    color = Color(0xFFD3E2F8),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.width(9.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.author,
                    color = PrimaryText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "Opinión de muestra · ${review.date}",
                    color = MutedText,
                    fontSize = 9.sp,
                )
            }
            Text(
                text = "EJEMPLO",
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(AmberPanel)
                    .padding(horizontal = 7.dp, vertical = 4.dp),
                color = Color(0xFFE0D0AC),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp,
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "★".repeat(review.rating) + "☆".repeat(5 - review.rating),
            color = Gold,
            fontSize = 13.sp,
            letterSpacing = (-1).sp,
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = review.text,
            color = Color(0xFFBEC6D0),
            fontSize = 11.sp,
            lineHeight = 17.sp,
        )
        review.response?.let { response ->
            Spacer(Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topEnd = 7.dp, bottomEnd = 7.dp))
                    .background(Color(0xFF202834))
                    .border(
                        width = 2.dp,
                        color = Color(0xFF6685B1),
                        shape = RoundedCornerShape(topEnd = 7.dp, bottomEnd = 7.dp),
                    )
                    .padding(9.dp),
            ) {
                Text(
                    text = "Respuesta de la institución",
                    color = Color(0xFFBDCDE3),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = response,
                    color = MutedText,
                    fontSize = 10.sp,
                    lineHeight = 15.sp,
                )
            }
        }
    }
}

@Composable
private fun EmptyState(title: String, description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("⌕", color = Blue, fontSize = 24.sp)
        Spacer(Modifier.height(7.dp))
        Text(title, color = PrimaryText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Text(description, color = MutedText, fontSize = 10.sp)
    }
}

@Composable
private fun AboutCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF1B2430),
        shape = RoundedCornerShape(11.dp),
        border = BorderStroke(1.dp, Border),
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            SectionLabel("TRANSPARENCIA Y ALCANCE")
            Spacer(Modifier.height(7.dp))
            Text(
                text = "Una ventana a la experiencia ciudadana.",
                color = PrimaryText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = "Este prototipo usa ejemplos ficticios. Para mostrar reseñas reales se necesita una integración autorizada. Google Places API requiere una clave y puede devolver hasta cinco reseñas por lugar; no ofrece un archivo completo de opiniones.",
                color = MutedText,
                fontSize = 10.sp,
                lineHeight = 15.sp,
            )
        }
    }
}
