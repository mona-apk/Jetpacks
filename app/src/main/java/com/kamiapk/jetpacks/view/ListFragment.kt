package com.kamiapk.jetpacks.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.kamiapk.jetpacks.R
import com.kamiapk.jetpacks.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    //変数の宣言・初期化
    private lateinit var viewModel : ListViewModel
    private  val dogsListAdapter = DogsListAdapter(arrayListOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    //新しくオーバーライド
    //ここにViewModelの処理をする
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //フラグメント内でviewmodelインスタンスの取得
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        //refresh関数の呼び出し

        //viewmodelの方に詳しい記述はあるがデータクラスの準備をしている
        //ネットワーク通信&データ取得
        viewModel.refresh()

        //dogsListはリサイクラービューのid
        dogsList.apply{
            //リサイクラービューにどのようにアイテムを並べるかの設定
            layoutManager = LinearLayoutManager(context) //縦並べ
            //リサイクラービューのアダプターにdogsListAdapterをいれるが別の場所で内容を変更する記述が必要
            adapter = dogsListAdapter
        }
        //関数の呼び出し

        //上にスワイプした時の動作
        refreshlayout.setOnRefreshListener {
            //更新するときには、ロード中のスピナーだけを見せる
            dogsList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            //上にスワイプしたときは、DBから値を取得するのではなくて
            // リモートから値を取得するのがよい
            //ついった
            //viewModel.refresh()
            viewModel.refreshBypassCache()
            //これで無限にロードをすることがなくなる
            refreshlayout.isRefreshing = false
        }




        observerViewModel()

    }

    //この関数自体はアクティビティというかフラグメントが作成された時に一回しかよばれない。
    //画面更新しても呼ばれない
    fun observerViewModel() {
        Log.w("TTT","Check")
        //viewModel.dogsは
        // val dogs = MutableLiveData<List<DogBreed>>()
        //でDogBreedデータクラスを要素に持つリスト
        viewModel.dogs.observe(this, Observer {dogs ->
            dogs?.let{
                dogsList.visibility = View.VISIBLE
                //アイテムの更新
                dogsListAdapter.updateDogList(dogs)
            }
        })

        //エラー文を表示するかロード中を表すスピナーを表示するのか
        viewModel.dogsLoadError.observe(this, Observer { isError ->
            isError?.let{
                listError.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let{
                loadingView.visibility = if(it) View.VISIBLE else View.GONE
                if(it) {
                    listError.visibility = View.GONE
                    dogsList.visibility = View.GONE
                }
            }

        })
    }

}
