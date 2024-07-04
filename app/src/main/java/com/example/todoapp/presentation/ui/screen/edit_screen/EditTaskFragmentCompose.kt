package com.example.todoapp.presentation.ui.screen.edit_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todoapp.TodoApp
import com.example.todoapp.presentation.ui.screen.model.TodoViewModelFactory
import com.example.todoapp.presentation.ui.screen.edit_screen.viewmodel.EditTaskViewModel
import com.example.todoapp.presentation.ui.screen.edit_screen.composable_details.EditTaskScreen
import com.example.todoapp.presentation.ui.theme.AppTheme

class EditTaskFragmentCompose : Fragment() {
    private val editTaskViewModel: EditTaskViewModel by viewModels {
        TodoViewModelFactory((requireActivity().application as TodoApp).todoItemsRepository)
    }

    private var todoId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        todoId = arguments?.getString(TASK_ID)
        editTaskViewModel.setTodoItem(todoId)

        val composeView = ComposeView(inflater.context)
        composeView.setContent {
            AppTheme {
                EditTaskScreen(editTaskViewModel) {
                    parentFragmentManager.popBackStack()
                    editTaskViewModel.clearData()
                }
            }
        }

        return composeView
    }


    companion object {
        const val TASK_ID = "taskId"
        fun newInstance(taskId: String?): EditTaskFragmentCompose {
            return EditTaskFragmentCompose().apply {
                arguments = Bundle().apply {
                    putString(TASK_ID, taskId)
                }
            }
        }
    }
}