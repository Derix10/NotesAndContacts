package com.example.experiments.ui.fragment.board

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.experiments.R
import com.example.experiments.base.BaseFragment
import com.example.experiments.databinding.FragmentOnBoardBinding
import com.example.experiments.ui.App
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class OnBoardFragment : BaseFragment<FragmentOnBoardBinding>(FragmentOnBoardBinding::inflate){

    private lateinit var adapter: BoardAdapter
    private var currentPage = 0


    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun setupObserver() {
        super.setupObserver()
        initGoogleSignClient()
        binding.btnNext.setOnClickListener{
            signIn()
            findNavController().navigateUp()
            App.prefs.saveBoardState()

            if (currentPage < 2){
                currentPage++
                when(currentPage){
                    0 -> binding.pagerOnBoard.currentItem = 0
                    1 -> binding.pagerOnBoard.currentItem = 1
                    2 -> binding.pagerOnBoard.currentItem = 2
                }
            }


        }

    }

    override fun setupUI() {

        adapter = BoardAdapter()
        binding.pagerOnBoard.adapter = adapter
        TabLayoutMediator(binding.mDots, binding.pagerOnBoard){ _, _ -> }.attach()



        binding.pagerOnBoard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {

                if (position == 0) currentPage = 0
                if (position == 1) currentPage = 1
                if (position == 2) {
                    binding.btnNext.visibility = View.VISIBLE
                }else{
                    binding.btnNext.visibility = View.GONE
                }
                super.onPageSelected(position)
            }
        })
    }



    private fun initGoogleSignClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        auth = Firebase.auth

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                account.idToken?.let { firebaseAuthWithGoogle(it) }
            }catch (e : ApiException){
                Log.d("ololo", "This is ololo: $e")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credentional = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credentional)
            .addOnCompleteListener(requireActivity()) { task ->
                Log.d("ololo", "This is task : $task")
                if (task.isSuccessful){
                    controller.navigateUp()
                    Toast.makeText(requireContext(), "Успешно", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show()
                }
            }

    }
    private fun signIn(){
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    companion object{
        private const val RC_SIGN_IN = 9001
    }
}