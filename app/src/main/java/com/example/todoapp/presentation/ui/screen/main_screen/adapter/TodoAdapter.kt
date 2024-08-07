package com.example.todoapp.presentation.ui.screen.main_screen.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.databinding.TodoItemBinding
import java.time.LocalDateTime

/**
 * TodoAdapter - класс адаптер для работы с RecyclerView, включает также TodoViewHolder и TodoDiffCallback
 */
class TodoAdapter(
    private val onTasksChangedListener: OnTaskChangeListener,
    private val onTaskPressListener: OnTaskPressListener
) :

    ListAdapter<TodoItem, TodoAdapter.TodoViewHolder>(TodoDiffCallback()) {

    interface OnTaskChangeListener {
        fun onTaskChanged(id: String)
    }

    interface OnTaskPressListener {
        fun onTaskPressed(id: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, onTasksChangedListener, onTaskPressListener)
    }


    class TodoViewHolder(binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tv = binding.mainText
        private val checkBox = binding.checkboxCompleted
        private val tvImportance = binding.importanceFlag
        private val editClickArea = binding.editTaskClickArea

        fun bind(
            todoItem: TodoItem,
            onTasksChangedListener: OnTaskChangeListener,
            onTaskPressListener: OnTaskPressListener,
        ) {
            tv.text = todoItem.text
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = todoItem.done
            updateTextDecoration(todoItem.done, todoItem.deadline)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                onTasksChangedListener.onTaskChanged(todoItem.id)
                updateTextDecoration(isChecked, todoItem.deadline)
            }
            editClickArea.setOnClickListener {
                onTaskPressListener.onTaskPressed(todoItem.id)
            }
            setUpImportance(todoItem.importance)
        }

        private fun setUpImportance(importance: Importance) {
            val context = itemView.context
            when (importance) {
                Importance.LOW -> {
                    tvImportance.text = context.getString(R.string.empty_string)
                }

                Importance.BASIC -> {
                    tvImportance.text = context.getString(R.string.medium_importance_symbol)
                    tvImportance.setTextColor(ContextCompat.getColor(context, R.color.label_primary))
                }

                Importance.IMPORTANT -> {
                    tvImportance.text = context.getString(R.string.high_importance_symbol)
                    tvImportance.setTextColor(ContextCompat.getColor(context, R.color.color_red))
                }
            }
        }


        private fun updateTextDecoration(done: Boolean, deadline: LocalDateTime?) {
            val context = itemView.context
            if (done) {
                tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tv.setTextColor(ContextCompat.getColor(context, R.color.label_tertiary))
            } else if (deadline != null && deadline <= LocalDateTime.now()) {
                tv.paintFlags = tv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                tv.setTextColor(ContextCompat.getColor(context, R.color.color_red))
            } else {
                tv.paintFlags = tv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                tv.setTextColor(ContextCompat.getColor(context, R.color.label_primary))
            }
        }
    }


    class TodoDiffCallback : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem == newItem && oldItem.done == newItem.done
        }
    }

}
