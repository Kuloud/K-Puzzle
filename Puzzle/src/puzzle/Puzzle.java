/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Puzzle.java
 *
 * Created on 2011-4-30, 18:45:45
 */
package puzzle;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Kuloud
 */
public class Puzzle extends javax.swing.JApplet implements MouseListener, KeyListener {

        
    //选择图片
    private String[] iNames;
    private int index;
    
    //定义难度，难度等级为分块的个数
    private int level =3;
    private int iNum = level * level;
    //定义每张小拼图的宽和高
    private int blockWidth = 360 / level;
    private int blockHeight = 360 / level;
    private final int viewWidth = 130;
    private final int viewHeight = 130;
    //定义图片对象
    private Image[] smallImages = new Image[iNum];
    private Image initImage;
    private Image viewImage;
    //标识各个拼图的排列情况
    private int[][] iSign = new int[level][level];
    private final int NO_IMAGE = -1;
    //初始化操作常量
    private final int UP = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;
    private final int RIGHT = 4;
    private final int NO_MOVE = -1;
    //步数计数器
    private int nStep;
    //标识两种状态的音乐
    AudioClip auMove, auNotMove;
    //标识是否完成拼图
    private boolean bWin;

    /** Initializes the applet Puzzle */
    @Override
    public void init() {

        //初始化参数
        bWin = false;
        nStep = 0;
        index = 3;
        level = 3;
        iNames = new String[] {"Hydrangeas","Koala","Lighthouse","Ayumi Hamasaki"};
        
        //加载声音
        auMove = getAudioClip(getDocumentBase(), "au/move.au");
        auNotMove = getAudioClip(getDocumentBase(), "au/notmove.au");

        getInitImages(index);
        
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    initComponents();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initData();
        addMouseListener(this);
        addKeyListener(this);
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, 600, 360);
        for (int i = 0; i < level; i++) {
            for (int j = 0; j < level; j++) {
                int y = i * blockHeight;
                int x = j * blockWidth;
                if (iSign[i][j] == NO_IMAGE) {
                    g.fill3DRect(x, y, blockWidth, blockHeight, true);
                } else {
                    g.drawImage(smallImages[iSign[i][j]], x, y, this);
                    g.drawRect(x, y, blockWidth, blockHeight);
                }
            }
        }
        g.drawImage(viewImage, 370, 10, this);
        g.drawRect(370, 10, viewWidth, viewHeight);


        g.drawString("步数：" + nStep, 370, viewHeight + 30);

        if (bWin) {
            g.drawString("恭喜你完成拼图！", 370, viewHeight + 60);
            initData();
        }
    }

    public void initData() {
        for (int i = 0; i < level; i++) {
            for (int j = 0; j < level; j++) {
                iSign[i][j] = i * level + j;
            }
        }
        int x1 = 0, y1 = 0;
        int x2, y2;
        for (int i = 0; i < 60; i++) {
            x1 = (int) (Math.random() * level);
            y1 = (int) (Math.random() * level);
            x2 = (int) (Math.random() * level);
            y2 = (int) (Math.random() * level);

            int temp = iSign[x1][y1];
            iSign[x1][y1] = iSign[x2][y2];
            iSign[x2][y2] = temp;
        }
        iSign[x1][y1] = NO_IMAGE;
        nStep = 0;
    }

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent me) {

        int mpX = me.getX();
        int mpY = me.getY();
        int nRow = mpY / blockHeight;
        int nCol = mpX / blockWidth;
        int op = getOperate(nRow, nCol);
        if (op != NO_MOVE) {
            move(nRow, nCol, op);
            if (!bWin) {
                nStep++;
            }
            auMove.play();
        } else {
            auNotMove.play();
        }

        repaint();

        checkStatus();
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    private void move(int row, int col, int op) {
        //col和row为要移动的拼图的位置
        switch (op) {
            case UP:
                iSign[row - 1][col] = iSign[row][col];
                iSign[row][col] = NO_IMAGE;
                break;
            case DOWN:
                iSign[row + 1][col] = iSign[row][col];
                iSign[row][col] = NO_IMAGE;
                break;
            case LEFT:
                iSign[row][col - 1] = iSign[row][col];
                iSign[row][col] = NO_IMAGE;
                break;
            case RIGHT:
                iSign[row][col + 1] = iSign[row][col];
                iSign[row][col] = NO_IMAGE;
                break;
            default:
                break;
        }
    }

    private int getOperate(int row, int col) {
        if (row < 0 || row >= level || col < 0 || col >= level) {
            return NO_MOVE;
        }
        if ((row - 1) >= 0) {
            if (iSign[row - 1][col] == NO_IMAGE) {
                return UP;
            }
        }
        if ((row + 1) < level) {
            if (iSign[row + 1][col] == NO_IMAGE) {
                return DOWN;
            }
        }
        if ((col - 1) >= 0) {
            if (iSign[row][col - 1] == NO_IMAGE) {
                return LEFT;
            }
        }
        if ((col + 1) < level) {
            if (iSign[row][col + 1] == NO_IMAGE) {
                return RIGHT;
            }
        }
        return NO_MOVE;
    }

    //往某个方向移动拼图
    private boolean move(int op) {

        int noImageCol = -1;
        int noImageRow = -1;
        int i = 0;
        int j = 0;

        //判断空白的块
        while (i < level && noImageRow == -1) {
            while (j < level && noImageCol == -1) {
                if (iSign[i][j] == NO_IMAGE) {
                    noImageRow = i;
                    noImageCol = j;
                }
                j++;
            }
            j = 0;
            i++;
        }

        //可以移动的位置为第noImageCol列，第noImageRow行
        switch (op) {

            case UP:
                if (noImageRow == level - 1) {
                    return false;
                }
                iSign[noImageRow][noImageCol] = iSign[noImageRow + 1][noImageCol];
                iSign[noImageRow + 1][noImageCol] = NO_IMAGE;
                break;
            case DOWN:
                if (noImageRow == 0) {
                    return false;
                }
                iSign[noImageRow][noImageCol] = iSign[noImageRow - 1][noImageCol];
                iSign[noImageRow - 1][noImageCol] = NO_IMAGE;
                break;
            case LEFT:
                if (noImageCol == level - 1) {
                    return false;
                }
                iSign[noImageRow][noImageCol] = iSign[noImageRow][noImageCol + 1];
                iSign[noImageRow][noImageCol + 1] = NO_IMAGE;
                break;
            case RIGHT:
                if (noImageCol == 0) {
                    return false;
                }
                iSign[noImageRow][noImageCol] = iSign[noImageRow][noImageCol - 1];
                iSign[noImageRow][noImageCol - 1] = NO_IMAGE;
                break;
            default:
                break;
        }
        return true;
    }

    private void checkStatus() {
        bWin = true;
        //定义成员，默认值为真
        int nCorrectNum = 0;
        for (int j = 0; j < level; j++) {
            for (int i = 0; i < level; i++) {
                if (iSign[j][i] != nCorrectNum
                        && iSign[j][i] != NO_IMAGE) {
                    bWin = false;
                    return;
                }
                nCorrectNum++;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int op = NO_MOVE;
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                op = DOWN;
                break;
            case KeyEvent.VK_UP:
                op = UP;
                break;
            case KeyEvent.VK_LEFT:
                op = LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                op = RIGHT;
                break;
            case KeyEvent.VK_F1:
                //F1键按下，重新开始游戏
                initData();
                repaint();
                return;
            case KeyEvent.VK_P:
                index = (index - 1+iNames.length) % iNames.length;
                getInitImages(index);
                repaint();
                return;
            case KeyEvent.VK_N:
                index = (index + 1) % iNames.length;
                getInitImages(index);
                repaint();
                return;
            default:
                return;
        }

        if (move(op)) {
            if (!bWin) {
                nStep++;
            }
            auMove.play();
        } else {
            auNotMove.play();
        }

        repaint();

        checkStatus();
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    private void getInitImages(int index) {
        //建立一个监视器
        MediaTracker mt = new MediaTracker(this);
        //装载总的大图片
        initImage = getImage(getDocumentBase(), "images/" + iNames[index]+".jpg");

        mt.addImage(initImage, 1);
        try {
            mt.waitForAll();
        } catch (InterruptedException ex) {
            System.out.println("图片加载错误！");
        }
        int smallWidth = initImage.getWidth(this) / level;
        int smallHeight = initImage.getHeight(this) / level;

        //遍历小拼图对象，装载每一个
        for (int i = 0; i < iNum; i++) {
            //创建实例，为每个小拼图分配内存空间
            smallImages[i] = createImage(blockWidth, blockHeight);
            //获得Graphic对象
            Graphics g = smallImages[i].getGraphics();
            //算出每块小图对应的区域
            int nRow = i / level;
            int nCol = i % level;
            //往拼图上画
            g.drawImage(initImage, 0, 0, blockWidth, blockHeight,
                    nCol * smallWidth, nRow * smallHeight,
                    (nCol + 1) * smallWidth, (nRow + 1) * smallHeight, this);
        }

        viewImage = createImage(viewWidth, viewHeight);
        Graphics g = viewImage.getGraphics();
        g.drawImage(initImage, 0, 0, viewWidth, viewHeight, 0, 0, initImage.getWidth(this), initImage.getHeight(this), this);
    }

}
