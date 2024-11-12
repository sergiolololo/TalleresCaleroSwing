package com.taller.utils;

import java.util.List;
import javax.swing.JTextField;
import javax.swing.text.*;

public class Java2sAutoTextField extends JTextField {
    class AutoDocument extends PlainDocument {

        public void replace(int i, int j, String s, AttributeSet attributeset)
                throws BadLocationException {
            super.remove(i, j);
            insertString(i, s, attributeset);
        }

        public void insertString(int i, String s, AttributeSet attributeset)
                throws BadLocationException {
            if (s == null || s.isEmpty())
                return;
            String s1 = getText(0, i);
            String s2 = getMatch(s1 + s);
            int j = (i + s.length()) - 1;
            if (isStrict && s2 == null) {
                s2 = getMatch(s1);
                j--;
            } else if (!isStrict && s2 == null) {
                super.insertString(i, s, attributeset);
                return;
            }
            super.remove(0, getLength());
            super.insertString(0, s2, attributeset);
            setSelectionStart(j + 1);
            setSelectionEnd(getLength());
        }

        public void remove(int i, int j) throws BadLocationException {
            super.remove(i, j);
        }

    }

    public Java2sAutoTextField(List<String> list) {
        isCaseSensitive = false;
        isStrict = false;
        if (list == null) {
            throw new IllegalArgumentException("values can not be null");
        } else {
            dataList = list;
            setDocument(new AutoDocument());
        }
    }

    private String getMatch(String s) {
        for (Object o : dataList) {
            String s1 = o.toString();
            if (s1 != null) {
                if (!isCaseSensitive
                        && s1.toLowerCase().startsWith(s.toLowerCase()))
                    return s1;
                if (isCaseSensitive && s1.startsWith(s))
                    return s1;
            }
        }

        return null;
    }

    public void replaceSelection(String s) {
        AutoDocument _lb = (AutoDocument) getDocument();
        if (_lb != null)
            try {
                int i = Math.min(getCaret().getDot(), getCaret().getMark());
                int j = Math.max(getCaret().getDot(), getCaret().getMark());
                _lb.replace(i, j - i, s, null);
            } catch (Exception ignored) {
            }
    }

    public void setDataList(List<String> list) {
        if (list == null) {
            throw new IllegalArgumentException("values can not be null");
        } else {
            dataList = list;
        }
    }

    private List<String> dataList;

    private final boolean isCaseSensitive;

    private final boolean isStrict;
}