import java.security.Security;
//import java.util.Base64;
import java.util.ArrayList;
import com.google.gson.GsonBuilder;


public class Yeetcoin {
    
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 4;
    public static Wallet walletA;
    public static Wallet walletB;

    public static void main(String[]args) {
        //Setup Bouncey castle as a security provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //Create the new wallets
        walletA = new Wallet();
        walletB = new Wallet();
        //Test public and private keys
        System.out.println("Private and public keys: ");
        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(StringUtil.getStringFromKey(walletA.publicKey));
        //Create a new transaction from Wallet a to wallet b
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);     
        //Verify the signature works and verify it is from the public key
        System.out.println("Is signature verified");
        System.out.println(transaction.verifySignature());   
        
        //add our blocks to the blockchain ArrayList:

        blockchain.add(new Block("data for first block", "0"));
        System.out.println("Trying to Mine block 1... ");
        blockchain.get(0).mineBlock(difficulty);
        
        blockchain.add(new Block("data for second block", blockchain.get(blockchain.size()-1).hash));
        System.out.println("Trying to Mine block 2... ");
        blockchain.get(1).mineBlock(difficulty);
        
        blockchain.add(new Block("data for the third block", blockchain.get(blockchain.size()-1).hash));
        System.out.println("Trying to Mine block 3... ");
        blockchain.get(2).mineBlock(difficulty);

        /* 
        blockchain.add(new Block("data for the fourth block", blockchain.get(blockchain.size()-1).hash));
        System.out.println("Trying to mine block 4... ");
        blockchain.get(2).mineBlock(difficulty);
        */

        System.out.println("\nBlockchain is Valid: " + isChainValid());

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);     
        
    }

    
    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0','0');

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.subSequence(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;        
    }
        
}
