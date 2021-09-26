package uz.java.maniac.asaxiy_bot.model.temp;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.java.maniac.asaxiy_bot.model.Lang;
import uz.java.maniac.asaxiy_bot.model.json.Root;
import uz.java.maniac.asaxiy_bot.service.UnirestHelper;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TempRoot {
    @Autowired
    private UnirestHelper helper;

    private RootModel rootUZ;
    private RootModel rootOZ;
    private RootModel rootRU;

    public void setAllRoot(){
        RootModel rootModel=new RootModel();
        Root rootUZ = helper.getRootCategory(Lang.UZ);
        Root rootOZ = helper.getRootCategory(Lang.OZ);
        Root rootRU =  helper.getRootCategory(Lang.RU);
        if (rootUZ != null) this.rootUZ=rootModel.setRootCategory(rootUZ);
        if (rootOZ != null) this.rootOZ=rootModel.setRootCategory(rootOZ);
        if (rootRU != null) this.rootRU=rootModel.setRootCategory(rootRU);
    }

    public RootModel get(Lang lang){
        if (lang==null) return null;
        if (lang==Lang.UZ) return rootUZ;
        if (lang==Lang.OZ) return rootOZ;
        if (lang==Lang.RU) return rootRU;
        return null;
    }

}
