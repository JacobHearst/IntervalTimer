package com.example.intervaltimer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.room.Workout

/**
 * A simple [Fragment] subclass.
 */
class LandingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mockWorkout = Workout(1, "Workout 1", 300)

        this.findNavController().navigate(LandingFragmentDirections.actionLandingFragmentToIntervalListFragment(mockWorkout))

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing, container, false)
    }


}
