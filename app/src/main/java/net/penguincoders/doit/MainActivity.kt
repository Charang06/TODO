package net.penguincoders.doit

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import net.penguincoders.doit.Adapters.ToDoAdapter
import net.penguincoders.doit.AddNewTask.Companion.newInstance
import net.penguincoders.doit.Model.ToDoModel
import net.penguincoders.doit.Utils.DatabaseHandler
import java.util.Collections
import java.util.Objects

class MainActivity : AppCompatActivity(), DialogCloseListener {
    private var db: DatabaseHandler? = null
    private var tasksRecyclerView: RecyclerView? = null
    private var tasksAdapter: ToDoAdapter? = null
    private var fab: FloatingActionButton? = null
    private var taskList: List<ToDoModel?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Objects.requireNonNull(supportActionBar).hide()
        db = DatabaseHandler(this)
        db!!.openDatabase()
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.setLayoutManager(LinearLayoutManager(this))
        tasksAdapter = ToDoAdapter(db!!, this@MainActivity)
        tasksRecyclerView.setAdapter(tasksAdapter)
        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(tasksAdapter))
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)
        fab = findViewById(R.id.fab)
        taskList = db!!.allTasks
        Collections.reverse(taskList)
        tasksAdapter!!.setTasks(taskList as MutableList<ToDoModel>?)
        fab.setOnClickListener(View.OnClickListener {
            newInstance().show(
                supportFragmentManager, AddNewTask.TAG
            )
        })
    }

    override fun handleDialogClose(dialog: DialogInterface?) {
        taskList = db!!.allTasks
        Collections.reverse(taskList)
        tasksAdapter!!.setTasks(taskList)
        tasksAdapter!!.notifyDataSetChanged()
    }
}