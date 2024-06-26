package com.example.todoapp.presentation.ui.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import java.time.LocalDateTime


class TodoAdapter(

    private val todoItemsRepository: TodoItemsRepository,
    private val onTasksChangedListener: OnTasksChangeListener,
    private val onTaskEditListener: OnTaskEditListener
) :

    ListAdapter<TodoItem, TodoAdapter.TodoViewHolder>(TodoDiffCallback()) {

    interface OnTasksChangeListener {
        fun onTasksChanged()
    }

    interface OnTaskEditListener {
        fun onTaskEdit(id: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {

        val currentItem = getItem(position)

        holder.bind(currentItem, onTasksChangedListener, onTaskEditListener, todoItemsRepository)
    }


    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTodoText: TextView = itemView.findViewById(R.id.mainText)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkboxCompleted)
        private val tvImportance: TextView = itemView.findViewById(R.id.importanceFlag)
        private val editClickArea: LinearLayout = itemView.findViewById(R.id.editTaskClickArea)

        fun bind(
            todoItem: TodoItem,
            onTasksChangedListener: OnTasksChangeListener,
            onTaskEditListener: OnTaskEditListener,
            todoItemsRepository: TodoItemsRepository
        ) {
            tvTodoText.text = todoItem.text
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = todoItem.done
            updateTextDecoration(tvTodoText, todoItem.done)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (checkBox.isChecked != todoItem.done) {
                    todoItemsRepository.changeTodoItemDoneStatus(todoItem.id)

                    updateTextDecoration(tvTodoText, isChecked)
                    onTasksChangedListener.onTasksChanged()
                }
            }

            editClickArea.setOnClickListener {
                onTaskEditListener.onTaskEdit(todoItem.id)
            }

            when (todoItem.importance) {
                Importance.LOW -> {
                    tvImportance.text = ""
                    tvImportance.setTextColor(Color.BLACK)
                }

                Importance.MEDIUM -> {
                    tvImportance.text = "!"
                    tvImportance.setTextColor(Color.BLACK)
                }

                Importance.HIGH -> {
                    tvImportance.text = "!!"
                    tvImportance.setTextColor(Color.RED)
                }
            }

            if (!todoItem.done && todoItem.deadline != null && todoItem.deadline <= LocalDateTime.now()) {
                tvTodoText.setTextColor(Color.RED)
            } else {
                tvTodoText.setTextColor(Color.BLACK)
            }
        }


        private fun updateTextDecoration(tv: TextView, done: Boolean) {
            if (done) {
                tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tv.paintFlags = tv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }


    class TodoDiffCallback : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem == newItem
        }
    }

}
