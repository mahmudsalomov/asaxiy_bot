package uz.java.maniac.asaxiy_bot.model.temp;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.java.maniac.asaxiy_bot.model.Lang;
import uz.java.maniac.asaxiy_bot.model.json.Category;
import uz.java.maniac.asaxiy_bot.model.json.Root;
import uz.java.maniac.asaxiy_bot.model.json.SubRoot;
import uz.java.maniac.asaxiy_bot.service.UnirestHelper;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TempRoot {
    @Autowired
    private UnirestHelper helper;

    public RootModel rootUZ;
    public RootModel rootOZ;
    public RootModel rootRU;

    public void setAllRoot() throws InterruptedException {
        RootModel rootModel=new RootModel();
        Root rootUZ = helper.getRootCategory(Lang.UZ);
        Root rootOZ = helper.getRootCategory(Lang.OZ);
        Root rootRU =  helper.getRootCategory(Lang.RU);
        if (rootUZ != null) this.rootUZ=new RootModel().setRootCategory(rootUZ);
        if (rootOZ != null) this.rootOZ=new RootModel().setRootCategory(rootOZ);
        if (rootRU != null) this.rootRU=new RootModel().setRootCategory(rootRU);

        if (rootOZ != null) {
            for (int i = 0; i <rootOZ.data.categories.size() ; i++) {
                SubRoot subRoot = helper.getSubCategory(Lang.OZ, rootOZ.data.categories.get(i).id);


                if(subRoot != null){
                    List<Category> subCategory = subRoot.getData();
                    if (subCategory!=null){
                        rootOZ.data.categories.get(i).setChildren(subCategory);

                        System.out.println(" Ota = "+rootOZ.data.categories.get(i).getId()+" ****************");
                        rootOZ.data.categories.get(i).children.forEach(c->System.out.println("sub "+c.id));
                        Thread.sleep(1000);
                    }
                }

            }
        }

        if (rootUZ != null) {
            for (int i = 0; i <rootUZ.data.categories.size() ; i++) {
                SubRoot subRoot = helper.getSubCategory(Lang.UZ, rootUZ.data.categories.get(i).id);

                if (subRoot!=null){
                    List<Category> subCategory = subRoot.getData();
                    if (subCategory!=null){
                        rootUZ.data.categories.get(i).setChildren(subCategory);

                        System.out.println(" Ota = "+rootUZ.data.categories.get(i).getId()+" ****************");
                        rootUZ.data.categories.get(i).children.forEach(c->System.out.println("sub "+c.id));

                        Thread.sleep(1000);
                    }
                }

            }
        }

        if (rootRU != null) {
            for (int i = 0; i <rootRU.data.categories.size() ; i++) {
                SubRoot subRoot = helper.getSubCategory(Lang.RU, rootRU.data.categories.get(i).id);

                if (subRoot!=null){
                    List<Category> subCategory = subRoot.getData();
                    if (subCategory!=null){
                        rootRU.data.categories.get(i).setChildren(subCategory);

                        System.out.println(" Ota = "+rootRU.data.categories.get(i).getId()+" ****************");
                        rootRU.data.categories.get(i).children.forEach(c->System.out.println("sub "+c.id));
                        Thread.sleep(1000);
                    }
                }
            }
        }

    }

    public RootModel get(Lang lang){
        if (lang==null) return null;
        if (lang==Lang.UZ) return rootUZ;
        if (lang==Lang.OZ) return rootOZ;
        if (lang==Lang.RU) return rootRU;
        return null;
    }

    public boolean exists(){
        return existsUz() && existsOz() && existsRu();
    }

    public boolean existsUz(){
        return this.rootUZ != null;
    }

    public boolean existsOz(){
        return this.rootOZ != null;
    }

    public boolean existsRu(){
        return this.rootRU != null;
    }


}
