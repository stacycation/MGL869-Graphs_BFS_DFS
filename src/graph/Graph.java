/**
 * Class Graph
 * @author Roberto E. Lopez-Herrejon
 * ETS MGL869
 */
package graph;

import java.io.*;
import java.util.LinkedList;

public class Graph {
	
    public Reader inFile; // File handler for reading
    public static int ch; // Character to read/write

    // Lists of vertices and edges
    public LinkedList<Vertex> vertices;
    public LinkedList<Edge> edges;
    
    //Search result
    public LinkedList<Vertex> searchOrder;
    private int startIndex;
    
	// **************************************************
	// Management of graph file for benchmarking
	// @feature BENCH
    
    /**
     * Opens the benchmark file
     * @param FileName
     */
    public void openBenchmark( String FileName ) {
        try {
            inFile = new FileReader( FileName );
        } catch ( IOException e ) {
            System.out.println( "Your file " + FileName + " cannot be read" );
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Attempts to close the benchmark file.
     * @throws IOException
     */
    public void stopBenchmark()
    {
    	try { inFile.close();
    	} catch(IOException e) {
    		System.out.println("Error while closing the benchmark file");
    		e.printStackTrace();
    		System.exit(0);
    	}
    }

    public int readNumber() throws IOException {
        int index =0;
        char[] word = new char[80];
        int ch=0;

        ch = inFile.read();
        while( ch==32 )
            ch = inFile.read(); // skips extra whitespaces

        while( ch!=-1 && ch!=32 && ch!=10 ) // while it is not EOF, WS, NL
        {
            word[index++] = ( char )ch;
            ch = inFile.read();
        }
        word[index]=0;

        String theString = new String( word );

        theString = new String( theString.substring( 0,index )).trim();
        return Integer.parseInt( theString,10 );
    }
 

    
    /**
     * Constructor of the Graph class that initializes the vertices and edges. 
     */
    public Graph() {
        vertices = new LinkedList<Vertex>();
        edges = new LinkedList<Edge>();
        searchOrder = new LinkedList<Vertex>();
    }
    

    /** 
     * Adds an edge without weights
     * @param start vertex of the edge
     * @param end vertex of the edge
     */
    public void addAnEdge( Vertex start,  Vertex end) {
        Edge theEdge = new  Edge(start,end);
        addEdge( theEdge );
    }
   
    /** 
     * Adds an edge with weights
     * @param start vertex of the edge
     * @param end vertex of the edge
     */
    public void addAnEdge( Vertex start,  Vertex end, int weight ) {
        Edge theEdge = new Edge(start, end, weight );
        addEdge(theEdge);
    }
    
    /**
     * Adds a vertex to the list of vertices
     * @param v Vertex to add
     */
    public void addVertex( Vertex v ) {
        vertices.add( v );
    }
   
    /**
     * Adds an edge based from
     * @param the_edge
     */
    public void addEdge( Edge the_edge ) {
        Vertex start = the_edge.start;
        Vertex end = the_edge.end;
        edges.add( the_edge );
        
        start.addNeighbor( new  Neighbor( end,the_edge ) );
        
        // if the graph is undirected
        if (GraphMain.UNDIRECTED) end.addNeighbor( new  Neighbor( start,the_edge ) );
        // ---
    } //of addEdge
    
 
    /**
     * Finds a vertex given its name in the vertices list
     * @param theName
     * @return
     */
    public  Vertex findsVertex( String theName ) {
    
        // if we are dealing with the root
        if ( theName==null ) return null;
        
        for (Vertex theVertex : vertices)  
        	if ( theName.equals( theVertex.name ) ) return theVertex;
        return null;
    } // of findsVertex

     
    /**
     * Displays the list of vertices, edges, and search order
     */
    public void display() {
        int i;
                                   
        System.out.println( "******************************************" );
        System.out.println( "Vertices " );
        for ( i=0; i<vertices.size(); i++ )
            ( ( Vertex ) vertices.get( i ) ).display();
         
        System.out.println( "******************************************" );
        System.out.println( "Edges " );
        for ( i=0; i<edges.size(); i++ )
            ( ( Edge ) edges.get( i ) ).display();
                
        System.out.println( "******************************************" );
        System.out.println( "Search Order " );
        for ( i=0; i<searchOrder.size(); i++ )
            System.out.printf(searchOrder.get( i ).name + " ");
    } // of display
    
     
   // **********************************************
   // **********************************************  
   // Implement your search methods here
    
    /**
     * Your implementation of DFS
     * Note that this methods displays each node in the traversal order
     * @param vertexName
     */
    
    
    public void DFS(String vertexName) {
    	//System.out.println("\n--------start DFS--------------------");
    	Vertex startVertex = findsVertex(vertexName);
    	Vertex currentVertex = startVertex;
    	this.startIndex = vertices.indexOf(startVertex);
    	int currentIndex;
    	
    	//System.out.println("vertices.size(): " + vertices.size());
    	//System.out.println("searchOrder.size(): " + searchOrder.size());
		//System.out.println("currentVertex.neighbors.size(): " + currentVertex.neighbors.size());
		

		for (int i=0; i < vertices.size(); i++) {
    		currentIndex = i + startIndex;
    		
    		if (currentIndex >= vertices.size()) {
    			currentIndex = currentIndex - i - startIndex;
    		}
    		
    		currentVertex = vertices.get(currentIndex);
    		
    		if (!currentVertex.visited) {
    			DFSRecursion(currentVertex);
    		} else {
    			continue;
    		}
    		

    	}

    } // of DFS 
    
    		
	public void DFSRecursion(Vertex currentVertex) {
		//System.out.println("\n-----recursion call------");
		if (!currentVertex.visited) {
			searchOrder.add(currentVertex);
			currentVertex.visited = true;
			
			//System.out.printf("current vertex added: ");
			//currentVertex.display();
			//System.out.println("searchOrder size: " + searchOrder.size());
		}
		
		
		int currentIndex = vertices.indexOf(currentVertex);
		
		if (currentVertex.neighbors.size() != 0 ) {
			for (int j = 0; j < currentVertex.neighbors.size(); j++) {
				//System.out.println("j: " + j);
				Vertex neighborVertex = currentVertex.neighbors.get(j).end;
				
				//System.out.printf("currentVertex: ");
				//currentVertex.display();
				//System.out.println("searchOrder.size(): " + searchOrder.size());
				
				//System.out.printf("neighborVertex: ");
				neighborVertex.display();
				if (neighborVertex.visited) {
	    			//System.out.println("neighborVertex.visited=true");
					continue; // go to next iteration of j loop
				} else {
					searchOrder.add(neighborVertex);
					neighborVertex.visited = true;
					
					//System.out.printf("neighborVertex added: ");
					//neighborVertex.display();
					//System.out.println("searchOrder size: " + searchOrder.size());
					
					//System.out.println("inside j loop");
					DFSRecursion( neighborVertex);
				}
			}
		} else {
			//currentIndex = vertices.indexOf(currentVertex);
			
			if (currentIndex < vertices.size()-1) {
				currentIndex = currentIndex+1;
			} else {
				currentIndex = currentIndex-this.startIndex;
			}
			
			Vertex nextVertex = vertices.get(currentIndex);
			if (!nextVertex.visited) {
				//System.out.println("inside recurse else");
				DFSRecursion(nextVertex);
			} else {
				return;
			}
			
		}
	}
	
    
    /**
     * Your implementation of BFS
     * Note that this methods displays each node in the traversal order
     * @param vertexName
     */
    public void BFS(String vertexName) {
    	//System.out.println("\n--------start BFS--------------------");
    	
    	Vertex startVertex = findsVertex(vertexName);
    	Vertex currentVertex;
    	this.startIndex = vertices.indexOf(startVertex);
    	int currentIndex = startIndex;
    	
    	//System.out.println("vertices.size(): " + vertices.size());
    	//System.out.println("startIndex: " + startIndex);
    	
    	
    	for (int i=0; i < vertices.size(); i++) {

    		currentIndex = startIndex + i;
    		//System.out.println("i: " + i  + ", currentIndex: " + currentIndex);
    		
    		//logic to loop back to the beginning of the list if we are past the end of the list
    		if (currentIndex >= vertices.size()) {
    			currentIndex = currentIndex - vertices.size();
    		}
    		
    		currentVertex = vertices.get(currentIndex);
    		
    		//System.out.printf("currentVertex: ");
    		//currentVertex.display();
    		//System.out.println("currentVertex.visited: " + currentVertex.visited);
    		
    		if (currentVertex.visited == true) {
    			continue;
    		} else {
	    		searchOrder.add(currentVertex);
	    		currentVertex.visited = true;
	    		
				//System.out.printf("currentVertex added: ");
				//currentVertex.display();
				//System.out.println("searchOrder size: " + searchOrder.size());
    		}
    		
    		BFSRecursion(currentVertex);
    	}

    } // of BFS    
    
    private void BFSRecursion(Vertex currentVertex) {
		//looking for neighbours
    	//System.out.println("\n------BFSrecursion------");
		//
		//System.out.printf("currentVertex: ");
		//currentVertex.display();
		
    	
		for (int j=0; j < currentVertex.neighbors.size(); j++) {
			//System.out.println("------new j------");
			//System.out.println("j: " + j);
			//System.out.println("currentVertex.neighbors.size(): " + currentVertex.neighbors.size());    			
			
			Vertex neighborVertex = currentVertex.neighbors.get(j).end;
			
			if (neighborVertex.visited == true) {
				continue; //break loop if vertex already visited
			} else {
				searchOrder.add(neighborVertex);
				neighborVertex.visited = true;
				
				//System.out.printf("neighborVertex added: ");
				//neighborVertex.display();
				//System.out.println("searchOrder size: " + searchOrder.size());
			}
		}
		
		Vertex nextVertex = currentVertex.neighbors.get(0).end; 
		
		if (searchOrder.size() < vertices.size()) {
			BFSRecursion(nextVertex);
		}
		
    }
    
    
} // of Graph
