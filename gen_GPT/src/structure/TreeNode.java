package structure;

/*
    树节点的抽象类
 */

public abstract class TreeNode {
    //  树节点的名字 如：T0-A0
    final private String name;

    TreeNode(String name) {
        this.name = name;
    }

    /**
     * 返回其名字
     */
    public String getName() {
        return this.name;
    }

}
