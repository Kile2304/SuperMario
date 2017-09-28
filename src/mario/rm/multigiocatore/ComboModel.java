package mario.rm.multigiocatore;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author LENOVO
 */
public class ComboModel extends AbstractListModel implements ComboBoxModel{

    private String selectedItem;
    
    private String[] orgs;
    
    public ComboModel(String[] orgs){
        this.orgs = orgs;
        selectedItem = orgs[0];
    }
    
    @Override
    public int getSize() {
        return orgs.length;
    }

    @Override
    public Object getElementAt(int index) {
        return orgs[index];
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for(int i = 0; i < orgs.length; i++){
            if(anItem.equals(orgs[i])){
                selectedItem = orgs[i];
                break;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }
    
}
