import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Cache {

	public static int M, P, S, J, N, fifothing=-1;
	public static String R;
	public static Page pageArr[];
	public static BufferedReader br;
	
	public class Page{
		public Page(){
			valid=false;
			data=-1;
			timeRef=-1;
			proc=-1;
		}
		public boolean valid;
		public int data;
		public int timeRef;
		public int timeAdded;
		public int proc;
	
		
		
		public boolean contains(int addr,int proc){
			if(!valid)return false;
			if(this.proc!=proc)return false;
			if(addr>=data&&addr<data+P)
				return true;
			return false;
		}
	}
	
	public class Process{
		public int hits=0;
		public int misses=0;
		public double res=0;
		public int evictions=0;
	}
	public static int getRand() throws NumberFormatException, IOException{
		return Integer.parseInt(br.readLine());
	}
	public static int findPage() throws NumberFormatException, IOException{
		for(int i=pageArr.length-1;i>=0;i--){
			if(!pageArr[i].valid){
				return i;
			}
		}
		
		int min=Integer.MAX_VALUE,res=-1,max=0;
		if(R.compareTo("lru")==0){
			for(int i=0;i<M/P;i++){
				if(pageArr[i].timeRef<=min){
					min=pageArr[i].timeRef;
					res=i;
				}
			}
			return res;
		}
		else if(R.compareTo("random")==0){
			return getRand()%(M/P);
		}
		else{
			if(fifothing==0){
				fifothing=M/P-1;
				return fifothing;
			}
			return --fifothing;
		}
			
	}
	public static void main(String args[]) throws NumberFormatException, IOException{
		br = new BufferedReader(new FileReader("random.txt"));
			M=Integer.parseInt(args[0]);
			P=Integer.parseInt(args[1]);
			S=Integer.parseInt(args[2]);
			J=Integer.parseInt(args[3]);
			N=Integer.parseInt(args[4]);
			R=args[5];
		pageArr=new Page[M/P];
		System.out.println("The machine size is "+M+".");
		System.out.println("The page size is "+P+".");
		System.out.println("The process size is "+S+".");
		System.out.println("The job mix number is "+J+".");
		System.out.println("The number of references per process is "+N+".");
		System.out.println("The replacement algorithm is "+R+".");
		System.out.println();
		for(int i=0;i<M/P;i++){
			pageArr[i]=new Cache().new Page();
		}
		if(J==1){
			int curRunning=0;
			int curAddr;
			Process proc;
				proc=new Cache().new Process();
				curAddr=111%S;
			int time=0;
			for(;time<N;time++){
				boolean found=false;
				for(int j=0;j<M/P;j++){
					if(pageArr[j].contains(curAddr,0)){
						pageArr[j].timeRef=time;
						proc.hits++;
						found=true;
					}
				}
					if(!found){
						int freepage=findPage();
						fifothing=freepage;
						proc.misses++;
						if(pageArr[freepage].valid){
							proc.res+=time-pageArr[freepage].timeAdded;
							proc.evictions++;
						}
						pageArr[freepage].valid=true;
						pageArr[freepage].timeRef=time;
						pageArr[freepage].timeAdded=time;
						pageArr[freepage].data=curAddr-curAddr%P;
						pageArr[freepage].proc=curRunning;
					}
					curAddr++;
					curAddr%=S;
				getRand();
			}
			if(proc.evictions==0){
				System.out.println("Process 1 had "+ proc.misses + " faults. With no evictions, the average residence is undefined.");
				System.out.println("The total number of faults is "+ proc.misses + ". With no evictions, the overall average residence is undefined.");
			}else{
				System.out.println("Process 1 had "+ proc.misses + " faults and "+ proc.res/proc.evictions + " average residency");
				System.out.println("The total number of faults is "+ proc.misses + " and the overall average residency is "+ proc.res/proc.evictions);
			}

	}
		if(J==2){
			int curRunning=0;
			int curAddr[]=new int[4];
			Process procArr[]=new Process[4];
			for(int i=0;i<4;i++){
				procArr[i]=new Cache().new Process();
				curAddr[i]=(111*(i+1))%S;
			}
			int time=0;
			for(;time<(N-N%3)*4;time++){
				boolean found=false;
				for(int j=0;j<M/P;j++){
					if(pageArr[j].contains(curAddr[curRunning],curRunning)){
						pageArr[j].timeRef=time;
						procArr[curRunning].hits++;
						found=true;
					}
				}
					if(!found){
						int freepage=findPage();
						fifothing=freepage;
						procArr[curRunning].misses++;
						if(pageArr[freepage].valid){
							procArr[pageArr[freepage].proc].res+=time-pageArr[freepage].timeAdded;
							procArr[pageArr[freepage].proc].evictions++;
						}
						pageArr[freepage].valid=true;
						pageArr[freepage].timeRef=time;
						pageArr[freepage].timeAdded=time;
						pageArr[freepage].data=curAddr[curRunning]-curAddr[curRunning]%P;
						pageArr[freepage].proc=curRunning;
					}
					curAddr[curRunning]++;
					curAddr[curRunning]%=S;
			
				
				if(time%3==2){
					curRunning++;
					curRunning%=4;
				}
				
				getRand();
			}
			for(;time<N*4;time++){
				boolean found=false;
				for(int j=0;j<M/P;j++){
					if(pageArr[j].contains(curAddr[curRunning],curRunning)){
						pageArr[j].timeRef=time;
						procArr[curRunning].hits++;
						found=true;
					}
					
				}
					if(!found){
						int freepage=findPage();
						fifothing=freepage;
						procArr[curRunning].misses++;
						if(pageArr[freepage].valid){
							procArr[pageArr[freepage].proc].res+=time-pageArr[freepage].timeAdded;
							procArr[pageArr[freepage].proc].evictions++;
						}
						pageArr[freepage].valid=true;
						pageArr[freepage].timeRef=time;
						pageArr[freepage].timeAdded=time;
						pageArr[freepage].data=curAddr[curRunning]-curAddr[curRunning]%P;
						pageArr[freepage].proc=curRunning;
					}
					curAddr[curRunning]++;
					curAddr[curRunning]%=S;
				
				if(time%(N%3)==(N%3)-1){
					curRunning++;
					curRunning%=4;
				}
				getRand();
			}
			int misses=0;
			double residency=0,temp=0;
			for(int i=0;i<4;i++){
				if(procArr[i].evictions==0){
					System.out.println("Process "+i+" had "+ procArr[i].misses + " faults. With no evictions, the average residence is undefined.");
					misses+=procArr[i].misses;
				}else{
					System.out.println("Process "+i+" had "+ procArr[i].misses + " faults and "+ procArr[i].res/procArr[i].evictions + " average residency");
					misses+=procArr[i].misses;
					residency+=procArr[i].res;
					temp+=procArr[i].evictions;
				}
			}
			if(temp==0){
				System.out.println("The total number of faults is "+ misses + ". With no evictions, the overall average residence is undefined.");
			}else{
				residency/=temp;
				System.out.println("The total number of faults is "+ misses + " and the overall average residency is "+ residency);
			}
		}
		if(J==3){
			int curRunning=0;
			int curAddr[]=new int[4];
			Process procArr[]=new Process[4];
			for(int i=0;i<4;i++){
				procArr[i]=new Cache().new Process();
				curAddr[i]=(111*(i+1))%S;
			}
			int time=0;
			for(;time<(N-N%3)*4;time++){
				boolean found=false;
				for(int j=0;j<M/P;j++){
					if(pageArr[j].contains(curAddr[curRunning],curRunning)){
						pageArr[j].timeRef=time;
						procArr[curRunning].hits++;
						found=true;
					}
				}
					if(!found){
						int freepage=findPage();
						fifothing=freepage;
						procArr[curRunning].misses++;
						if(pageArr[freepage].valid){
							procArr[pageArr[freepage].proc].res+=time-pageArr[freepage].timeAdded;
							procArr[pageArr[freepage].proc].evictions++;
						}
						pageArr[freepage].valid=true;
						pageArr[freepage].timeRef=time;
						pageArr[freepage].timeAdded=time;
						pageArr[freepage].data=curAddr[curRunning]-curAddr[curRunning]%P;
						pageArr[freepage].proc=curRunning;
					}
					curAddr[curRunning]=getRand()%S;
			
				
				if(time%3==2){
					curRunning++;
					curRunning%=4;
				}
			}
			for(;time<N*4;time++){
				boolean found=false;
				for(int j=0;j<M/P;j++){
					if(pageArr[j].contains(curAddr[curRunning],curRunning)){
						pageArr[j].timeRef=time;
						procArr[curRunning].hits++;
						found=true;
					}
					
				}
					if(!found){
						int freepage=findPage();
						fifothing=freepage;
						procArr[curRunning].misses++;
						if(pageArr[freepage].valid){
							procArr[pageArr[freepage].proc].res+=time-pageArr[freepage].timeAdded;
							procArr[pageArr[freepage].proc].evictions++;
						}
						pageArr[freepage].valid=true;
						pageArr[freepage].timeRef=time;
						pageArr[freepage].timeAdded=time;
						pageArr[freepage].data=curAddr[curRunning]-curAddr[curRunning]%P;
						pageArr[freepage].proc=curRunning;
					}
					curAddr[curRunning]=getRand()%S;
				
				if(time%(N%3)==(N%3)-1){
					curRunning++;
					curRunning%=4;
				}
			}
			int misses=0;
			double residency=0,temp=0;
			for(int i=0;i<4;i++){
				if(procArr[i].evictions==0){
					System.out.println("Process "+i+" had "+ procArr[i].misses + " faults. With no evictions, the average residence is undefined.");
					misses+=procArr[i].misses;
				}else{
					System.out.println("Process "+i+" had "+ procArr[i].misses + " faults and "+ procArr[i].res/procArr[i].evictions + " average residency");
					misses+=procArr[i].misses;
					residency+=procArr[i].res;
					temp+=procArr[i].evictions;
				}
			}
			if(temp==0){
				System.out.println("The total number of faults is "+ misses + ". With no evictions, the overall average residence is undefined.");
			}else{
				residency/=temp;
				System.out.println("The total number of faults is "+ misses + " and the overall average residency is "+ residency);
			}
		}
		if(J==4){
			int curRunning=0;
			int curAddr[]=new int[4];
			Process procArr[]=new Process[4];
			for(int i=0;i<4;i++){
				procArr[i]=new Cache().new Process();
				curAddr[i]=(111*(i+1))%S;
			}
			int time=0;
			for(;time<(N-N%3)*4;time++){
				boolean found=false;
				for(int j=0;j<M/P;j++){
					if(pageArr[j].contains(curAddr[curRunning],curRunning)){
						pageArr[j].timeRef=time;
						procArr[curRunning].hits++;
						found=true;
					}
				}
					if(!found){
						int freepage=findPage();
						fifothing=freepage;
						procArr[curRunning].misses++;
						if(pageArr[freepage].valid){
							procArr[pageArr[freepage].proc].res+=time-pageArr[freepage].timeAdded;
							procArr[pageArr[freepage].proc].evictions++;
						}
						pageArr[freepage].valid=true;
						pageArr[freepage].timeRef=time;
						pageArr[freepage].timeAdded=time;
						pageArr[freepage].data=curAddr[curRunning]-curAddr[curRunning]%P;
						pageArr[freepage].proc=curRunning;
					}
					int rand = getRand();
					double y= rand/(Integer.MAX_VALUE +1d);
					if(curRunning==0){
						if(y<0.75)	curAddr[curRunning]= (curAddr[curRunning]+1)%S;
						else 		curAddr[curRunning]= (curAddr[curRunning]-5+S)%S;
					}
					if(curRunning==1){
						if(y<0.75)	curAddr[curRunning]= (curAddr[curRunning]+1)%S;
						else 		curAddr[curRunning]= (curAddr[curRunning]+4)%S;
					}
					if(curRunning==2){
						if(y<0.75)		curAddr[curRunning]= (curAddr[curRunning]+1)%S;
						else if(y<0.875)curAddr[curRunning]= (curAddr[curRunning]-5+S)%S;
						else 			curAddr[curRunning]= (curAddr[curRunning]+4)%S;
					}
					if(curRunning==3){
						if(y<0.5)		curAddr[curRunning]= (curAddr[curRunning]+1)%S;
						else if(y<0.625)curAddr[curRunning]= (curAddr[curRunning]-5+S)%S;
						else if(y<0.75)	curAddr[curRunning]= (curAddr[curRunning]+4)%S;
						else 			curAddr[curRunning]= getRand()%S;

					}
				if(time%3==2){
					curRunning++;
					curRunning%=4;
				}
			}
			for(;time<N*4;time++){
				boolean found=false;
				for(int j=0;j<M/P;j++){
					if(pageArr[j].contains(curAddr[curRunning],curRunning)){
						pageArr[j].timeRef=time;
						procArr[curRunning].hits++;
						found=true;
					}
					
				}
					if(!found){
						int freepage=findPage();
						fifothing=freepage;
						procArr[curRunning].misses++;
						if(pageArr[freepage].valid){
							procArr[pageArr[freepage].proc].res+=time-pageArr[freepage].timeAdded;
							procArr[pageArr[freepage].proc].evictions++;
						}
						pageArr[freepage].valid=true;
						pageArr[freepage].timeRef=time;
						pageArr[freepage].timeAdded=time;
						pageArr[freepage].data=curAddr[curRunning]-curAddr[curRunning]%P;
						pageArr[freepage].proc=curRunning;
					}
					int rand = getRand();
					double y= rand/(Integer.MAX_VALUE +1d);
					if(curRunning==0){
						if(y<0.75)	curAddr[curRunning]= (curAddr[curRunning]+1)%S;
						else 		curAddr[curRunning]= (curAddr[curRunning]-5+S)%S;
					}
					if(curRunning==1){
						if(y<0.75)	curAddr[curRunning]= (curAddr[curRunning]+1)%S;
						else 		curAddr[curRunning]= (curAddr[curRunning]+4)%S;
					}
					if(curRunning==2){
						if(y<0.75)		curAddr[curRunning]= (curAddr[curRunning]+1)%S;
						else if(y<0.875)curAddr[curRunning]= (curAddr[curRunning]-5+S)%S;
						else 			curAddr[curRunning]= (curAddr[curRunning]+4)%S;
					}
					if(curRunning==3){
						if(y<0.5)		curAddr[curRunning]= (curAddr[curRunning]+1)%S;
						else if(y<0.625)curAddr[curRunning]= (curAddr[curRunning]-5+S)%S;
						else if(y<0.75)	curAddr[curRunning]= (curAddr[curRunning]+4)%S;
						else 			curAddr[curRunning]= getRand()%S;

					}
				
				if(time%(N%3)==(N%3)-1){
					curRunning++;
					curRunning%=4;
				}
			}
			int misses=0;
			double residency=0,temp=0;
			for(int i=0;i<4;i++){
				if(procArr[i].evictions==0){
					System.out.println("Process "+i+" had "+ procArr[i].misses + " faults. With no evictions, the average residence is undefined.");
					misses+=procArr[i].misses;
				}else{
					System.out.println("Process "+i+" had "+ procArr[i].misses + " faults and "+ procArr[i].res/procArr[i].evictions + " average residency");
					misses+=procArr[i].misses;
					residency+=procArr[i].res;
					temp+=procArr[i].evictions;
				}
			}
			if(temp==0){
				System.out.println("The total number of faults is "+ misses + ". With no evictions, the overall average residence is undefined.");
			}else{
				residency/=temp;
				System.out.println("The total number of faults is "+ misses + " and the overall average residency is "+ residency);
			}
		}
	}
	
}
