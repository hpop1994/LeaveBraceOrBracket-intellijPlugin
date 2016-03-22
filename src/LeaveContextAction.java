import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

/**
 * Created by xujc on 3/22/16.
 */
public class LeaveContextAction extends AnAction {
    private static final char[] START = {'(', '[', '{', '<'};
    private static final char[] END = {')', ']', '}', '>'};

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        CaretModel caretModel = editor.getCaretModel();
        Document document = editor.getDocument();
        int startOffset = caretModel.getOffset();
        CharSequence charsAfter = document.getCharsSequence().subSequence(startOffset, document.getTextLength());
        int length = START.length;
        int[] times = new int[length];
        for (int i = 0; i < charsAfter.length(); i++) {
            char c = charsAfter.charAt(i);
            for (int j = 0; j < length; j++) {
                if(c==START[j]){
                    times[j]++;
                } else if(c==END[j]){
                    if (times[j]<=0){
                        caretModel.moveToOffset(startOffset+i+1);
                        return;
                    } else {
                        times[j]--;
                    }
                }
            }
        }
        caretModel.moveToOffset(document.getTextLength()+1);
    }

    @Override
    public void update(final AnActionEvent e) {
        //Get required data keys
        final Project project = e.getData(CommonDataKeys.PROJECT);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        //Set visibility only in case of existing project and editor and if some text in the editor is selected
        e.getPresentation().setVisible((project != null && editor != null));
    }
}
