package com.example.todoapp.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.fragment.app.commit
import com.example.todoapp.R
import com.example.todoapp.presentation.ui.adapter.TodoAdapter
import com.example.todoapp.TodoApp
import com.example.todoapp.data.repository.TodoItemsRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), TodoAdapter.OnTaskEditListener {
    private lateinit var fab: FloatingActionButton
    private lateinit var todoItemsRepository: TodoItemsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoItemsRepository = (application as TodoApp).todoItemsRepository

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, MainFragment())
            }
        }

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            showAddTaskFragment()
        }
    }

    private fun showAddTaskFragment(taskId: String? = null) {
        fab.isEnabled = false
        fab.isInvisible = true

        val fragment = AddTaskFragment.newInstance(taskId)

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                fab.isEnabled = true
                fab.isInvisible = false
            }
        }

        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
        }

    }

    override fun onTaskEdit(id: String) {
        showAddTaskFragment(id)
    }
}
