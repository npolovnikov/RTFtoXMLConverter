package com.techinfocom.utils;

import com.techinfocom.utils.model.RtfCommand;
import com.techinfocom.utils.model.TextFormat;

import java.util.Stack;

/**
 * Created by volkov_kv on 27.05.2016.
 */
public class GroupState {
    private Stack<TextFormat> stack;

    public GroupState() {
        this.stack = new Stack<>();
    }

    public void addLevel() {
        if (!stack.empty()) {
            stack.push(new TextFormat(stack.peek())); //текущий формат войдет в формат верхнего уровня
        } else {
            stack.push(new TextFormat());
        }
    }

    public void removeLevel() {
        stack.pop();
    }

    public void processCommand(RtfCommand rtfCommand) {
        stack.peek().processCommand(rtfCommand);
    }

    public Integer getDepth() {
        return stack.size();
    }

    public TextFormat getCurrent() {
        TextFormat textFormat = new TextFormat();
        TextFormat current = stack.peek();
        textFormat.getFontFormat().addAll(current.getFontFormat());
        textFormat.getParagraphFormat().addAll(current.getParagraphFormat());
        return textFormat;
    }

    public String printCurrentLevel() {
        if (stack.isEmpty()){
            return new TextFormat().toString();
        } else {
            return getCurrent().toString();
        }
    }
}
