package app.softwork.bootstrapcompose.showcase

import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import app.softwork.bootstrapcompose.SpacingSpecs.SpacingSize.Medium
import org.jetbrains.compose.web.dom.*

@Composable

fun SelectView() {
    Container(styling = { Margins { Top { size = Medium } } }) {
        Row {
            Column(size = 4) {
                basicSelect()
                Hr(attrs = { classes("m-2") })
                basicSelect(size = SelectSize.Large)
                Hr(attrs = { classes("m-2") })
                basicSelect(size = SelectSize.Small)
                Hr(attrs = { classes("m-2") })
                basicSelect(disabled = true)
            }

            Column(size = 4) {
                basicSelect(multiple = true)
            }

            Column(size = 4) {
                basicSelect(rows = 3)
            }
        }
    }

}

@Composable
fun basicSelect(
    multiple: Boolean = false,
    size: SelectSize = SelectSize.Default,
    rows: Int? = null,
    disabled: Boolean = false
) {
    Select(size = size, rows = rows, multiple = multiple, disabled = disabled, onChange = { v -> println(v) }) {
        Option("") {
            Text("Open this select menu")
        }
        Option("1") {
            Text("One")
        }
        Option("2") {
            Text("Two")
        }
        Option("3") {
            Text("Three")
        }
    }
}
