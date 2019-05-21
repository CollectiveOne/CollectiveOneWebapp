package org.collectiveone.modules.ipld;

import java.security.MessageDigest;

import io.ipfs.cid.Cid;
import io.ipfs.multibase.Base58;
import io.ipfs.multibase.Multibase;
import io.ipfs.multibase.Multibase.Base;
import io.ipfs.multihash.Multihash;
import io.ipfs.multihash.Multihash.Type;

public class Ipld {
	
	public static Cid parse(String string) throws Exception {
		return new Cid(Multihash.deserialize(Multibase.decode(string)));
	}
	
	public static String hash(String json, Type type, Base base) throws Exception {
		String digest;
		
		switch (type) {
		
		case sha2_256:
			digest = "SHA-256";
		break;
		
		default: 
			throw new Exception("multihash type not supported");
		
		}
		
		MessageDigest digestInstance = MessageDigest.getInstance(digest);
		byte[] hashBytes = digestInstance.digest(json.getBytes());
		
		Multihash multihash = new Multihash(type, hashBytes);
		
		switch (base) {
		
		case Base58BTC:
			return Base58.encode(multihash.toBytes());
		
		default:
			throw new Exception("Unsuported base");
		
		}
	}

}
