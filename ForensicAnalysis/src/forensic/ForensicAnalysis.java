package forensic;

/**
 * This class represents a forensic analysis system that manages DNA data using
 * BSTs.
 * Contains methods to create, read, update, delete, and flag profiles.
 * 
 * @author Kal Pandit
 */
public class ForensicAnalysis {

    private TreeNode treeRoot;            // BST's root
    private String firstUnknownSequence;
    private String secondUnknownSequence;

    public ForensicAnalysis () {
        treeRoot = null;
        firstUnknownSequence = null;
        secondUnknownSequence = null;
    }

    /**
     * Builds a simplified forensic analysis database as a BST and populates unknown sequences.
     * The input file is formatted as follows:
     * 1. one line containing the number of people in the database, say p
     * 2. one line containing first unknown sequence
     * 3. one line containing second unknown sequence
     * 2. for each person (p), this method:
     * - reads the person's name
     * - calls buildSingleProfile to return a single profile.
     * - calls insertPerson on the profile built to insert into BST.
     *      Use the BST insertion algorithm from class to insert.
     * 
     * DO NOT EDIT this method, IMPLEMENT buildSingleProfile and insertPerson.
     * 
     * @param filename the name of the file to read from
     */
    public void buildTree(String filename) {
        // DO NOT EDIT THIS CODE
        StdIn.setFile(filename); // DO NOT remove this line

        // Reads unknown sequences
        String sequence1 = StdIn.readLine();
        firstUnknownSequence = sequence1;
        String sequence2 = StdIn.readLine();
        secondUnknownSequence = sequence2;
        
        int numberOfPeople = Integer.parseInt(StdIn.readLine()); 

        for (int i = 0; i < numberOfPeople; i++) {
            // Reads name, count of STRs
            String fname = StdIn.readString();
            String lname = StdIn.readString();
            String fullName = lname + ", " + fname;
            // Calls buildSingleProfile to create
            Profile profileToAdd = createSingleProfile();
            // Calls insertPerson on that profile: inserts a key-value pair (name, profile)
            insertPerson(fullName, profileToAdd);
        }
    }

    /** 
     * Reads ONE profile from input file and returns a new Profile.
     * Do not add a StdIn.setFile statement, that is done for you in buildTree.
    */
    public Profile createSingleProfile() 
    {

        int x = StdIn.readInt(); STR[] a = new STR[x];
        for(int j = 0; j < x; j++)
        {
            String n = StdIn.readString(); int o = StdIn.readInt(); STR newSTR = new STR(n, o); a[j] = newSTR;
        }
        Profile newProf = new Profile(a);
        
        return newProf; // update this line
    }

    /**
     * Inserts a node with a new (key, value) pair into
     * the binary search tree rooted at treeRoot.
     * 
     * Names are the keys, Profiles are the values.
     * USE the compareTo method on keys.
     * 
     * @param newProfile the profile to be inserted
     */
    public void insertPerson(String name, Profile newProfile) 
    {

     TreeNode newNode = new TreeNode(name, newProfile, null, null);
     if(treeRoot == null)
     {
         treeRoot = newNode;
     }
     else
     {
         insert(newNode, treeRoot);
     }
 
 }
 private void insert(TreeNode node1, TreeNode node2){
         
     int compare = node1.getName().compareTo(node2.getName());
     if(compare < 0)
     {
         if(node2.getLeft() == null)
         {
             node2.setLeft(node1);return;
         }

         else

         {
             node2 = node2.getLeft(); insert(node1, node2);
         }
     }

     if(compare > 0)

     {
         if(node2.getRight() == null)
         {
             node2.setRight(node1); return;
         }
         else
         {
             node2 = node2.getRight(); insert(node1, node2);
         }
     }
 }

    /**
     * Finds the number of profiles in the BST whose interest status matches
     * isOfInterest.
     *
     * @param isOfInterest the search mode: whether we are searching for unmarked or
     *                     marked profiles. true if yes, false otherwise
     * @return the number of profiles according to the search mode marked
     */
    public int getMatchingProfileCount(boolean isOfInterest) {
        
        if(treeRoot == null)
        {
            return 0;
        }
        else
        {
            if(isOfInterest == true)
            {
                return trueCount(treeRoot);
            }
            else
            {
                return falseCount(treeRoot);
            }
        }
    }
    private int trueCount(TreeNode node1)
    {
        if(node1 == null)
        {
            return 0;
        }
        else
        {
            if(node1.getProfile().getMarkedStatus() == true)
            {return 1 + trueCount(node1.getLeft()) + trueCount(node1.getRight());}
            else {return trueCount(node1.getLeft()) + trueCount(node1.getRight());}
        }
    }
    private int falseCount(TreeNode node1)
    {
        if(node1 == null)
        {
            return 0;
        }
        else
        {
            if(node1.getProfile().getMarkedStatus() == false)
            {return 1 + falseCount(node1.getLeft()) + falseCount(node1.getRight());}
            else{return falseCount(node1.getLeft()) + falseCount(node1.getRight());}
        }
    }


    /**
     * Helper method that counts the # of STR occurrences in a sequence.
     * Provided method - DO NOT UPDATE.
     * 
     * @param sequence the sequence to search
     * @param STR      the STR to count occurrences of
     * @return the number of times STR appears in sequence
     */
    private int numberOfOccurrences(String sequence, String STR) {
        
        // DO NOT EDIT THIS CODE
        
        int repeats = 0;
        // STRs can't be greater than a sequence
        if (STR.length() > sequence.length())
            return 0;
        
            // indexOf returns the first index of STR in sequence, -1 if not found
        int lastOccurrence = sequence.indexOf(STR);
        
        while (lastOccurrence != -1) {
            repeats++;
            // Move start index beyond the last found occurrence
            lastOccurrence = sequence.indexOf(STR, lastOccurrence + STR.length());
        }
        return repeats;
    }

    /**
     * Traverses the BST at treeRoot to mark profiles if:
     * - For each STR in profile STRs: at least half of STR occurrences match (round
     * UP)
     * - If occurrences THROUGHOUT DNA (first + second sequence combined) matches
     * occurrences, add a match
     */
    public void flagProfilesOfInterest() 
     {
        if(treeRoot == null)
        {
            return;
        }
        else
        {
            traversal(treeRoot);
        }
        
    }
    private void traversal(TreeNode node)
    {
        if(node == null)
        {
            return;
        }
        else
        {
            int STRarraysize = node.getProfile().getStrs().length;int STRnumber = STRarraysize / 2;
            if((STRarraysize % 2 != 0) && (STRarraysize != 1))
            {
                STRnumber = STRnumber + 1;
            }
            if(STRarraysize == 1)
            {
                STRnumber = 1;
            }
            int count = 0; for(int j = 0; j < STRarraysize; j++)
            {
                int firstSeqNum = numberOfOccurrences(firstUnknownSequence, node.getProfile().getStrs()[j].getStrString()); int SecSeqNum = numberOfOccurrences(secondUnknownSequence, node.getProfile().getStrs()[j].getStrString()); int seqRepeats = firstSeqNum + SecSeqNum;
                if(seqRepeats == node.getProfile().getStrs()[j].getOccurrences())
                {
                    count++;
                }
            }
            if(count >= STRnumber)
            {
                node.getProfile().setInterestStatus(true);
            }
            
            traversal(node.getLeft());
            traversal(node.getRight());
        }
        
    }


    /**
     * Uses a level-order traversal to populate an array of unmarked Strings representing unmarked people's names.
     * - USE the getMatchingProfileCount method to get the resulting array length.
     * - USE the provided Queue class to investigate a node and enqueue its
     * neighbors.
     * 
     * @return the array of unmarked people
     */
    public String[] getUnmarkedPeople() {

        int arrayLength = getMatchingProfileCount(false); String[] newArray = new String[arrayLength]; Queue<TreeNode> queued = new Queue<>(); int x = 0;
        if(treeRoot == null)
        {
            return null;
        }
        else
        {
            return traverseUnmarked(treeRoot, newArray, x, queued);
        }
    }
    private String[] traverseUnmarked(TreeNode node, String[] array, int x, Queue<TreeNode> queued)
    {
        
        if(node == null)
        {
            return null;
        }
        else
        {
            queued.enqueue(node); while(!queued.isEmpty())
            {
                TreeNode current = queued.dequeue();
                if(current.getProfile().getMarkedStatus() == false)
                {
                    array[x] = current.getName(); x++;
                }
                if(current.getLeft() != null)
                {
                    queued.enqueue(current.getLeft());
                }
                if(current.getRight() != null)
                {
                    queued.enqueue(current.getRight());
                }
            }
        }
        return array;
    }


    /**
     * Removes a SINGLE node from the BST rooted at treeRoot, given a full name (Last, First)
     * This is similar to the BST delete we have seen in class.
     * 
     * If a profile containing fullName doesn't exist, do nothing.
     * You may assume that all names are distinct.
     * 
     * @param fullName the full name of the person to delete
     */
    public void removePerson(String fullName) 
    {
        if(treeRoot == null){
            return;
        }
        treeRoot = delete(treeRoot, fullName);
    }
    private TreeNode delete(TreeNode node, String name)
    {
        if(node == null)
        {
            return null;
        }
        int cmp = node.getName().compareTo(name);
        if(cmp > 0)
        {
            node.setLeft(delete(node.getLeft(), name));
        }
        else if(cmp < 0)
        {
            node.setRight(delete(node.getRight(), name));
        }
        else
        {
            if(node.getRight() == null)
            { /* no right child */
                return node.getLeft();
            }
            else if(node.getLeft() == null)
            { /* no left child */
                return node.getRight();
            }
            else
            { /* two children */
                TreeNode x = node; node = successor(x.getRight()); node.setRight(deleteMin(x.getRight())); node.setLeft(x.getLeft());
            }
        }
        return node;
    }
    private TreeNode successor(TreeNode node)
    {
        while(node.getLeft() != null)
        {
            node = node.getLeft();
        }
        return node;
    }
    private TreeNode deleteMin(TreeNode node)
    {
        if(node.getLeft() == null)
        {
            return node.getRight();
        }
        node.setLeft(deleteMin(node.getLeft()));
        return node;
    }

    

    /**
     * Clean up the tree by using previously written methods to remove unmarked
     * profiles.
     * Requires the use of getUnmarkedPeople and removePerson.
     */
    public void cleanupTree() 
    {
        String[] unmarkedArray = getUnmarkedPeople();
        int i = 0;
        while(i < unmarkedArray.length){
            removePerson(unmarkedArray[i]);
            i++;
        }
    }

    /**
     * Gets the root of the binary search tree.
     *
     * @return The root of the binary search tree.
     */
    public TreeNode getTreeRoot() {
        return treeRoot;
    }

    /**
     * Sets the root of the binary search tree.
     *
     * @param newRoot The new root of the binary search tree.
     */
    public void setTreeRoot(TreeNode newRoot) {
        treeRoot = newRoot;
    }

    /**
     * Gets the first unknown sequence.
     * 
     * @return the first unknown sequence.
     */
    public String getFirstUnknownSequence() {
        return firstUnknownSequence;
    }

    /**
     * Sets the first unknown sequence.
     * 
     * @param newFirst the value to set.
     */
    public void setFirstUnknownSequence(String newFirst) {
        firstUnknownSequence = newFirst;
    }

    /**
     * Gets the second unknown sequence.
     * 
     * @return the second unknown sequence.
     */
    public String getSecondUnknownSequence() {
        return secondUnknownSequence;
    }

    /**
     * Sets the second unknown sequence.
     * 
     * @param newSecond the value to set.
     */
    public void setSecondUnknownSequence(String newSecond) {
        secondUnknownSequence = newSecond;
    }

}