package kz.kuanysh.alartest.presentation.data_list

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.kuanysh.alartest.R
import kz.kuanysh.alartest.databinding.FragmentDataListBinding
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val LIST_END_OFFSET = 5

class DataListFragment : Fragment() {

    private var _binding: FragmentDataListBinding? = null
    private val binding get() = _binding!!

    private val safeArgs by navArgs<DataListFragmentArgs>()

    private val viewModel by viewModel<DataListViewModel> {
        parametersOf(safeArgs.code)
    }

    private val adapter by lazy {
        DataRecyclerAdapter(onItemClicked = {
            viewModel.dispatch(DataListAction.ItemClicked(it))
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOptionMenu()
        initUI()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        adapter.onViewHolderBound = { position ->
            if (adapter.itemCount - position <= LIST_END_OFFSET) {
                viewModel.dispatch(DataListAction.LoadNextData)
            }
        }
        with(binding) {
            refreshLayout.setOnRefreshListener {
                viewModel.dispatch(DataListAction.UpdateData)
            }
            recyclerView.adapter = adapter
        }
    }

    private fun observeViewModel() {
        viewModel.dataListUIState.observe(viewLifecycleOwner) {
            when (it) {
                DataListUI.DataUpdating -> binding.refreshLayout.isRefreshing = true

                DataListUI.NextDataLoading -> binding.nextProgress.isVisible = true

                is DataListUI.DataUpdated -> {
                    binding.refreshLayout.isRefreshing = false
                    adapter.updateData(it.items)
                }

                is DataListUI.NextDataLoaded -> {
                    adapter.addItems(it.items)
                    binding.nextProgress.isVisible = false
                }

                is DataListUI.OpenDetails -> {
                    it.item.getContentIfNotHandled()?.let { item ->
                        findNavController().navigate(
                            DataListFragmentDirections
                                .actionDataListFragmentToDataDetailsFragment(item)
                        )
                    }
                }

                DataListUI.Logout -> {
                    findNavController().navigate(
                        DataListFragmentDirections.actionDataListFragmentToLoginFragment()
                    )
                }

                DataListUI.Error -> {
                    with(binding) {
                        refreshLayout.isRefreshing = false
                        nextProgress.isVisible = false
                    }
                }
            }
        }
    }

    private fun setupOptionMenu() {
        with(binding.toolbar) {
            inflateMenu(R.menu.menu_logout)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.logout -> {
                        viewModel.dispatch(DataListAction.Logout)
                        true
                    }
                    else -> super.onOptionsItemSelected(menuItem)
                }
            }
        }
    }
}