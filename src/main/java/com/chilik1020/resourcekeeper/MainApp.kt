
import com.chilik1020.resourcekeeper.di.DeviceModule

import com.chilik1020.resourcekeeper.ui.MainView
import com.chilik1020.resourcekeeper.utils.JsonConfig
import com.jtattoo.plaf.acryl.AcrylLookAndFeel
import di.AppComponent
import di.BufferModule
import di.DaggerAppComponent
import di.ViewModule
import javax.swing.UIManager

public class MainApp {
    companion object{
        lateinit var mainView: MainView
//            lateinit var injectorComponent:AppComponent
        val injectorComponent: AppComponent = DaggerAppComponent
                .builder()
                .bufferModule(BufferModule)
                .deviceModule(DeviceModule)
                .viewModule(ViewModule)
                .build()

        @JvmStatic fun main(args:Array<String>) {
            JsonConfig.readConfig()

            UIManager.setLookAndFeel(AcrylLookAndFeel())
//            UIManager.setLookAndFeel(MintLookAndFeel())
            mainView = MainView()
        }
    }

}
