package lua

import lua.estoque.categoria.Categoria

import java.util.LinkedList

import org.zkoss.zul.DefaultTreeNode
import org.zkoss.zul.TreeNode

class CategoryTreeNode extends DefaultTreeNode<Categoria>{
    private static final long serialVersionUID = 1L
    int count
    public CategoryTreeNode(Categoria category, int count) {
        super(category, new LinkedList<TreeNode<Categoria>>()) // assume not a leaf-node
        this.count = count
    }

    public String getDescription() {
        return getData().getDescription()
    }

    public int getCount() {
        return count
    }

    public boolean isLeaf() {
        return getData() != null && getData().getChildren().isEmpty()
    }
}
