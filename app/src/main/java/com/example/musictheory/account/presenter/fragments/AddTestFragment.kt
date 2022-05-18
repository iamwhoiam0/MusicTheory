package com.example.musictheory.account.presenter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.musictheory.MainActivity
import com.example.musictheory.R
import com.example.musictheory.account.presenter.viewmodels.PersonalAccountViewModel
import com.example.musictheory.core.data.MainActivityCallback
import com.example.musictheory.databinding.AddTestFragmentBinding
import com.example.musictheory.trainingtest.data.model.Question
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
@AndroidEntryPoint
class AddTestFragment : Fragment() {

    companion object {
        fun newInstance() = AddTestFragment()
    }

//    private lateinit var viewModel: AddTestViewModel
    private val personalAccountViewModel: PersonalAccountViewModel
    by hiltNavGraphViewModels<PersonalAccountViewModel>(R.id.nested_personal_account)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AddTestFragmentBinding.inflate(inflater)

        binding.buttonAddTestAccount.setOnClickListener {
            lifecycleScope.launch {
                val str = binding.editTextIncorrectAnswersAccount.text.toString()
                var strArray = str.split(", ")
                var answerArray = ArrayList<ArrayList<String>>()
                var tmpArray = arrayListOf(binding.editTextAnswerAccount.text.toString())
                tmpArray.addAll(strArray)
                answerArray.add(tmpArray)

                var token = ""
                if (activity is MainActivityCallback) {
                    token = (activity as MainActivityCallback).getToken()
                }
                if(token!="") {
//                    personalAccountViewModel.postTestToServer(
//                        listOf(binding.editTextQuestionAccount.text.toString()),
//                        answerArray,
//                        listOf("none"),
//                        binding.editTextTestNameAccount.text.toString()
//                    )
//

    var id = -1
                    if(!personalAccountViewModel.currentMusicOid.value.isNullOrBlank()){
                        id = personalAccountViewModel.currentMusicOid.value.toInt()
                    }
                    personalAccountViewModel.postTestToServer(
                        token,
                        binding.editTextTestNameAccount.text.toString(),
                        listOf("1"),
                        listOf(Question(
                            listOf("Секстаккорд", "квартсекстаккорд", "трезвучие"),
                            "",
                            listOf(),
                            mapOf(
                                "count" to 1,
                                "notes" to "from_answers_chords"
                            ),
                            "Какой это аккорд?",
                            "stave"
                        )),
                        "2",
                        id,
                    )
                }


                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        launch {
                            personalAccountViewModel.currentMusicOid.collect {
                                lifecycleScope.launch {
                                    val tests = async {
                                        var token = ""
                                        if (activity is MainActivityCallback) {
                                            token = (activity as MainActivityCallback).getToken()
                                        }
                                        personalAccountViewModel.getTests(token)
                                    }
                                    personalAccountViewModel.getData(tests.await())
                                    binding.editTextTestNameAccount.setText(personalAccountViewModel.musicTest.value.testName)
                                }
                            }
                        }

                    }
                }
//                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
//                    Timber.v("t1 repeat")
//                    launch {
//                        Timber.v("t1 aunch")
//                        val tests = async {
//                            var token = ""
//                            if (activity is MainActivityCallback) {
//                                token = (activity as MainActivityCallback).getToken()
//                            }
//                            personalAccountViewModel.getTests(token)
//                        }
//                        personalAccountViewModel.getData(tests.await())
//                        binding.editTextTestNameAccount.setText(personalAccountViewModel.musicTest.value.testName)
//                    }
//                }

//                val oid = arguments?.getString(MainActivity.testId)
//                personalAccountViewModel.setOid(oid.toString())
            }
        }



        binding.buttonDeleteTestAccount.setOnClickListener {
            lifecycleScope.launch {
                personalAccountViewModel.postDeleteTest()
            }
        }



                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        launch {
                            personalAccountViewModel.currentMusicOid.collect {
                                lifecycleScope.launch {
                                    val tests = async {
                                        var token = ""
                                        if (activity is MainActivityCallback) {
                                            token = (activity as MainActivityCallback).getToken()
                                        }
                                        personalAccountViewModel.getTests(token)
                                    }
                                    personalAccountViewModel.getData(tests.await())
                                    binding.editTextTestNameAccount.setText(personalAccountViewModel.musicTest.value.testName)
                                }
                            }
                        }

                    }
                }
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
//                Timber.v("t1 repeat")
//                launch {
//                    Timber.v("t1 aunch")
//                    val tests = async {
//                        var token = ""
//                        if (activity is MainActivityCallback) {
//                            token = (activity as MainActivityCallback).getToken()
//                        }
//                        personalAccountViewModel.getTests(token)
//                    }
//                    personalAccountViewModel.getData(tests.await())
//                    binding.editTextTestNameAccount.setText(personalAccountViewModel.musicTest.value.testName)
//                }
//            }
//        }


//                val oid = arguments?.getString(MainActivity.testId)
//                personalAccountViewModel.setOid(oid.toString())
        return binding.root
    }
}
