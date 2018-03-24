package lightoutpuzzlesolver;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class lightout
{
    int mat[][];
    int x,y;
    int n;
    JLabel l[][];
    Panel p,p2;
    Label count;
    Button sol;
    Random random;
    JFrame f;
    int cnt,cnt2;
    // another class object which is used to check if solution exist for given combinations
    solve solv;
    lightout()
    {
        cnt2=0;
        f=new JFrame();
        sol=new Button(" SOLVE ");
        String x;
        n=0;
        // taking size of board until its greater than 0
        while(n<=0)
        {
            try{
                x=JOptionPane.showInputDialog(f,"Enter size of BOARD ");
                this.n=Integer.parseInt(x);
            }
            catch(Exception e)
            {n=0;}
        }
        mat=new int[n][n];
        l=new JLabel[n][n];
        f.addWindowListener(new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        f.dispose();
                        solv.f1.dispose();
                        System.exit(0);
                    }
                });
        setvalue();
        p.setBounds(0,0,100*n,100*n);
        p2=new Panel();
        p2.setLayout(new FlowLayout(FlowLayout.CENTER));
        p2.setBounds(0,100*n,100*n,100);
        p2.add(sol);
        p2.add(count);
        sol.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                solv=new solve(mat,n);
                solv.f1.setVisible(true);
            }
        });
        f.add(p2);
        f.add(p);
        f.setLayout(null);
        f.setSize(100*n,100*n+100);
        f.setVisible(true);
    }
    void setvalue()
    {
        random=new Random();
        p=new Panel();
        p.setLayout(new GridLayout(n,n,5,5));
        boolean xx=true;
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                l[i][j]=new JLabel();
                int ii = random.nextInt(10);
                if(ii<5)
                {
                    mat[i][j]=0;
                    l[i][j].setBackground(Color.GREEN);
                    l[i][j].setOpaque(true);
                    p.add(l[i][j]);
                }
                else
                {
                    mat[i][j]=1;
                    xx=false;
                    l[i][j].setBackground(Color.black);
                    l[i][j].setOpaque(true);
                    p.add(l[i][j]);
                }
                l[i][j].addMouseListener(new MouseAdapter()
                {
                    public void mouseClicked(MouseEvent e)
                    {
                        int x=-1,y=0;
                        for(int i=0;i<n;i++)
                        {  for(int j=0;j<n;j++)
                                  if(e.getSource()==l[i][j])
                                  {
                                      x=i;
                                      y=j;
                                      break;
                                  }
                            if(x!=-1)
                                break;
                         }
                        flip(x,y);
                        cnt--;
                        cnt2++;
                        count.setText("Steps remaining : "+cnt);      
                        if(checkwin())
                        {
                            JDialog d=new JDialog(f ,"game Finished in "+cnt2+"  moves", true);
                            d.setSize(300,100);
                            JButton b=new JButton("exit");
                            d.add(b);
                            b.setBounds(10,10,150,50);
                            b.addActionListener(new ActionListener()
                            {
                                public void actionPerformed(ActionEvent e)
                                {
                                    f.dispose();
                                    solv.f1.dispose();
                                    System.exit(0);
                                }
                            });
                            d.setVisible(true);  
                        }
                        if(cnt==0)
                        {
                           JDialog d=new JDialog(f," YOU LOSE ",true);
                           d.setSize(300,100);
                            JButton b=new JButton(" coninue playing");
                            d.add(b);
                            b.setBounds(10,10,200,50);
                            b.addActionListener(new ActionListener()
                            {
                                public void actionPerformed(ActionEvent e)
                                {
                                     d.dispose();
                                }
                            });
                            d.setVisible(true);
                        }
                    }
                });
            }
        }
        // if solution doesn't exit or board is solved already call again the setvalue function
        solv=new solve(mat,n);
        if(solv.f==10000||xx)
           setvalue(); 
        else
        {
            cnt=solv.f;
            count=new Label("Steps remaining :  "+cnt);
        }
    }
    // flipping all 5 if exist piesce of board
    void flip(int i,int j)
    {
        int dr[]={0,1,-1,0,0};
        int dc[]={0,0,0,1,-1};
        for(int k=0;k<5;k++)
        {
            int x=i+dr[k];
            int y=j+dc[k];
            if(x>=0&&y>=0&&x<n&&y<n)
            {
                changebackground(x,y);
                mat[x][y]=mat[x][y]==1?0:1;
            }
        }
    }
    void changebackground(int x,int y)
    {
        if(mat[x][y]==1)
            l[x][y].setBackground(Color.green);
        else
            l[x][y].setBackground(Color.black);
    }
    // checking if they win
    boolean checkwin()
    {
        boolean f=true;
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                  if(mat[i][j]==1)
                      f=false;
        return f;
    }
    
}
class solve
{
     JFrame f1;
     JLabel ll[][];
     int dup[][];   // it is the duplicate of main matrix
     int tans[][];   // it is matrix that contain temporary changed values of main matrix each time
     int pans[][];  // it contain actual solution
     int n;
     int f;
     int mat[][];
     solve(int m[][],int nn)
     {
         f1=new JFrame();
         n=nn;
         mat=m;
         f1.setLayout(new GridLayout(n,n,5,5));
         f1.setSize(100*n,100*n);
         ll=new JLabel[n][n];
         dup=new int[n][n];
         makecopy();
         for(int i=0;i<n;i++)
             for(int j=0;j<n;j++)
             {
                 ll[i][j]=new JLabel();
                 ll[i][j].setOpaque(true);
                 ll[i][j].setBackground(Color.black);
                 ll[i][j].setForeground(Color.white);
                 f1.add(ll[i][j]);
             }
         f1.addWindowListener(new WindowAdapter()
         {
             public void windowClosing(WindowEvent e)
             {
                 f1.dispose();
             }
         });
         findsol();
     }
     private void findsol()
     {
        f=10000;
        int ff=0;
        for(int i=0;i<(1<<n);i++)
        {
            ff=0;
            makecopy();
            for(int j=0;j<n;j++)
            {
                if((i&(1<<j))!=0)
                {
                    flipp(0,j);
                    tans[0][j]=1;
                    ff++;
                }
            }
            for(int j=1;j<n;j++)
            {
                for(int k=0;k<n;k++)
                {
                    if(dup[j-1][k]==1)
                    {
                        flipp(j,k);
                        tans[j][k]=1;
                        ff++;
                    }
                }
            }
            boolean valid=true;
            for(int j=0;j<n;j++)
            {
                if(dup[n-1][j]==1)
                    valid=false;
            }
            if(valid&&ff<=f)
            {
                pans=tans;
                f=ff;
            }
        }
        if(f!=10000)
            for(int i=0;i<n;i++)
                for(int j=0;j<n;j++)
                    if(pans[i][j]==1)
                        ll[i][j].setText(" click me ");

     }
     private void makecopy()
     {
         tans=new int[n][n];
         for(int i=0;i<n;i++)
             for(int j=0;j<n;j++)
             {
                 dup[i][j]=mat[i][j];
                 tans[i][j]=0;
             }
     }
     void flipp(int i,int j)
    {
        int dr[]={0,1,-1,0,0};
        int dc[]={0,0,0,1,-1};
        for(int k=0;k<5;k++)
        {
            int x=i+dr[k];
            int y=j+dc[k];
            if(x>=0&&y>=0&&x<n&&y<n)
            {
                dup[x][y]=dup[x][y]==1?0:1;
            }
        }
    }
}
public class LightOutPuzzleSolver
{
    public static void main(String ar[])
    {
        new lightout();
    }
}