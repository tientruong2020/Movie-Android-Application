package com.group1.movieapplication.ui.profile

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.group1.movieapplication.R
import com.group1.movieapplication.data.repository.FirebaseRTDBRepository
import com.group1.movieapplication.model.user.User
import com.group1.movieapplication.ui.profile.Adapter.MyMovieAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment : Fragment(), View.OnClickListener {

    private val profileViewModel by viewModels<ProfileViewModel>()
    private var mutableUser = MutableLiveData<User>()
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var myMovieAdapter: MyMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        userReference = firebaseDatabase.reference.child(FirebaseRTDBRepository.USER_TBL)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_to_edit_profile.setOnClickListener(this)
        getUserProfile()
        initRecyclerView()
        getRatedMovieIdList()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.btn_to_edit_profile) -> {
                toEditProfile()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserProfile() {
        mutableUser = profileViewModel.getUserInfo()
        mutableUser.observe(viewLifecycleOwner, { user ->
            tw_user_email.text = user.email
            tw_fullname.text = user.firstname + " " + user.lastname
            if (user.avatar_uri == null || user.avatar_uri == "") {
                imv_avatar.setImageResource(R.drawable.defaultavatar)
            } else {
                val imgUri: Uri = Uri.parse(user.avatar_uri)
                Picasso.get().load(imgUri).into(imv_avatar)
            }
        })
    }

    private fun toEditProfile() {
        val bundle = Bundle()
        mutableUser.observe(viewLifecycleOwner, { user ->
            bundle.putSerializable("user_profile", user)
        })
        findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment, bundle)
    }


    private fun initRecyclerView() {
        myMovieAdapter = MyMovieAdapter(activity?.applicationContext!!)
        rcv_my_movie.adapter = myMovieAdapter
        rcv_my_movie.layoutManager =
            GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        myMovieAdapter.setClickListener(object : MyMovieAdapter.clickListener {
            override fun clickImage(movieID: String) {
                Timber.d(movieID)
                // findNavController().navigate(R.id.action_profileFragment_to_movieDetailFragment)
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToMovieDetailFragment(movieID)
                findNavController().navigate(action)
            }
        })
    }

    @SuppressLint("LogNotTimber")
    private fun getRatedMovieIdList() {
        profileViewModel.getRatedMovieIdList().observe(viewLifecycleOwner, {
            if (it != null) {
                getMovieImgList(it)
            }
        })
    }

    private fun getMovieImgList(movieIdList: ArrayList<String>) {
        profileViewModel.getMovieImgList(movieIdList)
        profileViewModel.movieList.observe(viewLifecycleOwner, {
            it?.let {
                myMovieAdapter.setData(it)
            }
        })
    }
}