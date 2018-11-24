package com.dicoding.finalsoccermatches.presentation.team.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.finalsoccermatches.R
import kotlinx.android.synthetic.main.fragment_team_detail_desc.*

class OverviewFragment : Fragment() {
    companion object {
        private const val DESC = "description"

        fun newInstance(description: String): OverviewFragment {
            val args = Bundle().also {
                it.putString(DESC, description)
            }
            return OverviewFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_team_detail_desc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            description.text = it.getString(DESC)
        }
    }
}