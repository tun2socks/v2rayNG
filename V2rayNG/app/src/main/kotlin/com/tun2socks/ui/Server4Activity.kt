package com.tun2socks.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import com.tun2socks.R
import com.tun2socks.dto.AngConfig
import com.tun2socks.util.AngConfigManager
import com.tun2socks.util.Utils
import kotlinx.android.synthetic.main.activity_server4.*
import org.jetbrains.anko.*


class Server4Activity : BaseActivity() {
    companion object {
        private const val REQUEST_SCAN = 1
    }

    var del_config: MenuItem? = null
    var save_config: MenuItem? = null

    private lateinit var configs: AngConfig
    private var edit_index: Int = -1 //当前编辑的服务器
    private var edit_guid: String = ""
    private var isRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server4)

        configs = AngConfigManager.configs
        edit_index = intent.getIntExtra("position", -1)
        isRunning = intent.getBooleanExtra("isRunning", false)
        title = getString(R.string.title_server)

        if (edit_index >= 0) {
            edit_guid = configs.vmess[edit_index].guid
            bindingServer(configs.vmess[edit_index])
        } else {
            clearServer()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * bingding seleced server config
     */
    fun bindingServer(vmess: AngConfig.VmessBean): Boolean {
        et_remarks.text = Utils.getEditable(vmess.remarks)

        et_address.text = Utils.getEditable(vmess.address)
        et_port.text = Utils.getEditable(vmess.port.toString())

        return true
    }

    /**
     * clear or init server config
     */
    fun clearServer(): Boolean {
        et_remarks.text = null
        et_address.text = null
        et_port.text = Utils.getEditable("10086")

        return true
    }

    /**
     * save server config
     */
    fun saveServer(): Boolean {
        val vmess: AngConfig.VmessBean
        if (edit_index >= 0) {
            vmess = configs.vmess[edit_index]
        } else {
            vmess = AngConfig.VmessBean()
        }

        vmess.guid = edit_guid
        vmess.remarks = et_remarks.text.toString()
        vmess.address = et_address.text.toString()
        vmess.port = Utils.parseInt(et_port.text.toString())

        if (TextUtils.isEmpty(vmess.remarks)) {
            toast(R.string.server_lab_remarks)
            return false
        }
        if (TextUtils.isEmpty(vmess.address)) {
            toast(R.string.server_lab_address3)
            return false
        }
        if (TextUtils.isEmpty(vmess.port.toString()) || vmess.port <= 0) {
            toast(R.string.server_lab_port3)
            return false
        }

        if (AngConfigManager.addSocksServer(vmess, edit_index) == 0) {
            toast(R.string.toast_success)
            finish()
            return true
        } else {
            toast(R.string.toast_failure)
            return false
        }
    }

    /**
     * save server config
     */
    fun deleteServer(): Boolean {
        if (edit_index >= 0) {
            alert(R.string.del_config_comfirm) {
                positiveButton(android.R.string.ok) {
                    if (AngConfigManager.removeServer(edit_index) == 0) {
                        toast(R.string.toast_success)
                        finish()
                    } else {
                        toast(R.string.toast_failure)
                    }
                }
                show()
            }
        } else {
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_server, menu)
        del_config = menu?.findItem(R.id.del_config)
        save_config = menu?.findItem(R.id.save_config)

        if (edit_index >= 0) {
            if (isRunning) {
                if (edit_index == configs.index) {
                    del_config?.isVisible = false
                    save_config?.isVisible = false
                }
            }
        } else {
            del_config?.isVisible = false
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.del_config -> {
            deleteServer()
            true
        }
        R.id.save_config -> {
            saveServer()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}