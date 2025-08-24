package com.sushilbx.navgurukulam.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sushilbx.navgurukulam.SyncScheduler
import com.sushilbx.navgurukulam.adapters.StudentAdapter
import com.sushilbx.navgurukulam.databinding.ActivityMainBinding
import com.sushilbx.navgurukulam.viewmodels.MainVMFactory
import com.sushilbx.navgurukulam.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm: MainViewModel by viewModels { MainVMFactory(application) }
    private val adapter = StudentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater); setContentView(binding.root)

        binding.rvStudents.layoutManager = LinearLayoutManager(this)
        binding.rvStudents.adapter = adapter

        vm.students.observe(this) { adapter.submitList(it) }

        binding.btnAdd.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            if (name.isNotEmpty()) vm.addStudent(name)
            binding.etName.setText("")
        }

        binding.btnRetry.setOnClickListener { SyncScheduler.retryNow(this) }
    }
}

