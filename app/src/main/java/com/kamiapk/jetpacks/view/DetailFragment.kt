package com.kamiapk.jetpacks.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

import com.kamiapk.jetpacks.R
import com.kamiapk.jetpacks.databinding.FragmentDetailBinding
import com.kamiapk.jetpacks.util.getProgressDrawable
import com.kamiapk.jetpacks.util.loadImage
import com.kamiapk.jetpacks.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment() {

    private lateinit var viewModel : DetailViewModel
    private var dogUuid = 0

    //onCreateView内にてレイアウトのinflateの時に使用する
    private lateinit var dataBinding : FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
        データバインディング時
         */
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root

        //通常時
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    //Viewの初期化はonViewCreatedで行うのがよい
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel.feche(dogUuid)より前に持ってくる
        arguments?.let{
            //argumentsがnullでないならListFragmentから来たdogUuidで書き換える
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        //dogUuidによりどのDogBreedを指しているのか特定する
        viewModel.feche(dogUuid)

        observeViewModel()

    }

    private fun observeViewModel(){
        //DetailFragmentのテキストの書き換え
        //dogLiveDataのデータクラスのプロパティを参照する
        viewModel.dogLiveData.observe(this, Observer { dog ->
            dog?.let{

                //この一行でおけ
                //最初のdogはfragment_detail.xmlで定義したdog
                dataBinding.dog = it



                //データバインディングによってここの記述が減る
                /*
                dogName.text = dog.dogBreed
                dogPurpose.text = dog.bredFor
                dogTemperament.text = dog.temperament
                dogLifeSpan.text = dog.lifeSpan
                //画像表示は拡張関数をUtil.ktに記述しているのでそれを使う.
                context?.let{
                    dogImage.loadImage(dog.imageUrl, getProgressDrawable(it))
                }

                //エラー。contextがnull許容型で型不一致のため
                //dogImage.loadImage(dog.imageUrl, getProgressDrawable(context))
                */
            }
        })
    }


}
