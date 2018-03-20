package com.mazing.com.cavasdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mazing.com.cavasdemo.treenode.BinaryTreeNode;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by user on 2017/11/16.
 */

public class TreeNodeActivity extends AppCompatActivity {

    BinaryTreeNode mTreeNode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createTree(null);
    }

    /**
     * 创建排序二叉树  左树值全部小于根节点  右树值全部大于根节点
     * @param values
     */
    private void createTree(ArrayList<Integer> values){
        mTreeNode = null;
        for(int i = 0; i < values.size(); i++){
            mTreeNode = addTreeNode(mTreeNode,values.get(i));
        }
    }

    /**
     * 添加节点
     * @param root
     * @param value
     */
    public BinaryTreeNode addTreeNode(BinaryTreeNode root, int value){
        if(root == null){
            mTreeNode = new BinaryTreeNode();
            mTreeNode.value = value;
        }
        else if(root.value > value){
            mTreeNode.leftTreeNode = addTreeNode(mTreeNode.leftTreeNode,value);
        }
        else if(root.value < value){
            mTreeNode.rightTreeNode = addTreeNode(mTreeNode.rightTreeNode,value);
        }

        return root;
    }

    /**
     * 前序遍历树
     */
    public ArrayList<Integer> preOrderTraverseTree(BinaryTreeNode root){
        ArrayList<Integer> list = new ArrayList<>();
        if(root == null){
           return list;
        }
        else{
            list.add(root.value);
            handel(root,list);
        }
        // Stack是一个后进先出的堆栈
//        Stack<BinaryTreeNode> stack = new Stack<BinaryTreeNode>();
//        stack.push(root);
//        while (!stack.empty()){
//            BinaryTreeNode node = stack.pop(); //从栈顶依次向下出栈
//            list.add(node.value);
//
//            if(node.leftTreeNode != null){
//                stack.push(node.leftTreeNode);
//            }
//            if(node.rightTreeNode != null){
//                stack.push(node.rightTreeNode);
//            }
//        }

        return list;
    }

    public void handel(BinaryTreeNode root,ArrayList<Integer> list){
        if(root == null){
            return;
        }
        else{
            list.add(root.value);
            handel(root.leftTreeNode,list);
            handel(root.rightTreeNode,list);
        }
    }
}
