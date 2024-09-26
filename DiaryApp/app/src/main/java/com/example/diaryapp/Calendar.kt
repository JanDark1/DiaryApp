package com.example.diaryapp

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@Composable
fun CalendarDesign(date: String) {
    // Ay numaralarını kısaltmalarla eşleştirir.
    val monthMap = mapOf(
        "01" to "OCA", "02" to "ŞUB", "03" to "MAR", "04" to "NİS",
        "05" to "MAY", "06" to "HAZ", "07" to "TEM", "08" to "AĞU",
        "09" to "EYL", "10" to "EKİ", "11" to "KAS", "12" to "ARA"
    )

    // Tarihi gün, ay ve yıl olarak böler.
    val dateParts = date.split("-")

    // Tarih sıralamasını bozmadan ay, gün ve yıl olarak alır.
    val (month, day, year) = if (dateParts.size == 3) {
        dateParts
    } else {
        // Hatalı formatı kontrol edin, doğru format değilse hata verir
        Log.e("NisCalendarDesign", "Geçersiz tarih formatı: $date")
        return
    }

    // Ay kısaltmasını monthMap den alır.
    val monthAbbreviation = monthMap[month] ?: month

    // Tema renklerini aldığımız kısım.
    val backgroundColor = MaterialTheme.colorScheme.background
    val monthBoxColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onBackground
    val monthTextColor = MaterialTheme.colorScheme.onPrimary

    Column(
        modifier = Modifier
            .width(45.dp) //Bu, Column'un genişliğini 45 dp olarak ayarlıyor. Diğer bileşenlerin bu genişliğe uyum sağlaması bekleniyor.
            .background(backgroundColor, RoundedCornerShape(4.dp)) //Arka plan rengi, temadan alınan backgroundColor ile ayarlanıyor ve köşeler 4 dp'lik bir yuvarlatma uygulanıyor.
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // İçindeki tüm bileşenlerin yatay olarak ortalanmasını sağlıyor.
        verticalArrangement = Arrangement.Center //Bileşenlerin dikey olarak ortalanmasını sağlıyor.
    ) {

        // Ay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp) //Bu Box, bulunduğu Column'un tüm genişliğini kaplıyor ve yüksekliği 18 dp olarak ayarladım.
                .background(monthBoxColor, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = monthAbbreviation,
                color = monthTextColor,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(1.dp))

        // Gün
        Text(
            text = day,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = textColor
        )

        Spacer(modifier = Modifier.height(1.dp))

        // Yıl
        Text(
            text = year,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
}



@Composable
fun CalendarDialog(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay -> //Buraki _ kullanılmayan bir paremetredir. Daha okunaklı olması için _ şekilde gösterdim.
            // Tarihi 2 haneli formatla
            val formattedDay = selectedDay.toString().padStart(2, '0')
            val formattedMonth = (selectedMonth + 1).toString().padStart(2, '0')
            val date = "$formattedMonth-$formattedDay-$selectedYear" // Ay-Gün-Yıl formatı
            onDateSelected(date)
        },
        year, month, day
    ).apply {
        setOnDismissListener { onDismissRequest() }
    }.show()
}


@Composable
fun CalendarText(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}