package com.yricky.powertrans

import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.yricky.powertrans.model.DataBaseModel
import com.yricky.powertrans.pojo.youdao.Entries
import com.yricky.powertrans.pojo.youdao.Reply
import com.yricky.powertrans.ui.theme.OhBgUiDlg
import com.yricky.powertrans.ui.theme.OhBgUiDlgDark
import com.yricky.powertrans.ui.theme.PowerTransTheme
import com.yricky.powertrans.viewmodel.TransViewModel

class MainActivity : ComponentActivity() {
    val viewModel:TransViewModel by lazy{
        ViewModelProvider(this).get(TransViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PowerTransTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ContentView()
                }
            }
        }
    }
    
    @Composable
    fun ContentView(){
        Column {
            val item by viewModel.rawJson.observeAsState()
            val word by viewModel.queryWorld.observeAsState()
            EditQueryWord(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),"翻译",word ?: ""){
                viewModel.query(it)
            }
            item?.let{
                Results(it)
            }
        }

    }
}

@Composable
fun Results(entries: List<Entries>){

    LazyColumn{
        items(entries){ entry ->
            Column(
                Modifier
                    .padding(8.dp, 4.dp, 8.dp, 4.dp)
                    .background(
                        color = if (isSystemInDarkTheme()) {
                            OhBgUiDlgDark
                        } else {
                            OhBgUiDlg
                        },
                        shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp)
                    )
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {

                Text(
                    text = entry.entry,
//                    fontSize = if(reply.data.query == entry.entry){ 24.sp }else{ 20.sp },
//                    fontWeight = if(reply.data.query == entry.entry){ FontWeight.Bold }else{ FontWeight.Normal }
                )
                Text(modifier = Modifier.fillMaxWidth(),text = entry.explain.replace(Regex("([a-z]+\\.[^.])"),"\n$1").trim(),fontSize = 16.sp)
            }
        }
    }

}

@Composable
fun EditQueryWord(
    modifier: Modifier = Modifier,
    hint:String = "",
    text:String = "",
    onValueChange:(String)->Unit){
    var et by remember { mutableStateOf(text) }
    OutlinedTextField(
        value = et,
        onValueChange = {
            val lineStr = it.replace("\n","")
            et = lineStr
            onValueChange.invoke(lineStr)
        },
        maxLines = 1,
        label = { Text(text = hint) },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PowerTransTheme {
    }
}