package com.max.pandaai.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.pandaai.databinding.ItemMessageUserBinding
import com.max.pandaai.databinding.ItemMessageAiBinding
import com.max.pandaai.model.ChatMessage
import java.text.SimpleDateFormat
import java.util.*

/**
 * ChatAdapter - RecyclerView adapter for chat messages
 * Displays user and AI messages in different layouts
 */
class ChatAdapter : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(ChatDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_AI = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isUser) VIEW_TYPE_USER else VIEW_TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val binding = ItemMessageUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            UserMessageViewHolder(binding)
        } else {
            val binding = ItemMessageAiBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            AiMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is UserMessageViewHolder -> holder.bind(message)
            is AiMessageViewHolder -> holder.bind(message)
        }
    }

    /**
     * ViewHolder for user messages
     */
    class UserMessageViewHolder(private val binding: ItemMessageUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            binding.messageText.text = message.text
            binding.timeText.text = formatTime(message.timestamp)
        }
    }

    /**
     * ViewHolder for AI messages
     */
    class AiMessageViewHolder(private val binding: ItemMessageAiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            binding.messageText.text = message.text
            binding.timeText.text = formatTime(message.timestamp)
        }
    }

    companion object {
        /**
         * Format timestamp to readable time
         */
        fun formatTime(timestamp: Long): String {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }
}

/**
 * DiffUtil callback for efficient list updates
 */
class ChatDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
    override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem == newItem
    }
}
