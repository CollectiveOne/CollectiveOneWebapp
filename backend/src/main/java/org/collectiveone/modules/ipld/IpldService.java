package org.collectiveone.modules.ipld;

import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.ipfs.cid.Cid;
import io.ipfs.cid.Cid.Codec;
import io.ipfs.multibase.Multibase;
import io.ipfs.multibase.Multibase.Base;
import io.ipfs.multihash.Multihash;
import io.ipfs.multihash.Multihash.Type;

@Service
public class IpldService {
	
	static private Base UPRTCL_DFT_BASE;
	
	static private long UPRTCL_CID_VERSION;
	
	static private Codec UPRTCL_CID_CODEC;
	
	@Value("${UPRTLC_DFT_BASE}")
	public void setUPRTCL_DFT_BASE(Base base) {
		UPRTCL_DFT_BASE = base;
	}
	
	@Value("${UPRTCL_CID_VERSION}")
	public void setUPRTCL_CID_VERSION(long version) {
		UPRTCL_CID_VERSION = version;
	}
	
	@Value("${UPRTCL_CID_CODEC}")
	public void setUPRTCL_CID_CODEC(Codec codec) {
		UPRTCL_CID_CODEC = codec;
	}
	
	public static Cid encode(String string) throws Exception {
		if (string == null) return null;
		return Cid.cast(Multibase.decode(string));
	}
	
	/** Returns the Cid String of a multihash using the default base */
	public static String decode(byte[] cidBytes) {
		if (cidBytes == null) return null;
		return Multibase.encode(UPRTCL_DFT_BASE, cidBytes);		
	}
	
	/** Returns the Cid String of a multihash using the default base */
	public static String decode(byte[] cidBytes, Base base) {
		if (cidBytes == null) return null;
		return Multibase.encode(base, cidBytes);		
	}
	
	/** Returns the multihash bytes */
	public static byte[] hash(String json, Type type) throws Exception {
		String digest;
		
		switch (type) {
		
		case sha2_256:
			digest = "SHA-256";
		break;
		
		case sha3_256:
			digest = "SHA3-256";
		break;
		
		default: 
			throw new Exception("multihash type " + type.toString() + " not supported");
		
		}
		
		MessageDigest digestInstance = MessageDigest.getInstance(digest);
		byte[] hashBytes = digestInstance.digest(json.getBytes());
		
		Cid cid = new Cid(UPRTCL_CID_VERSION, UPRTCL_CID_CODEC, new Multihash(type, hashBytes));
		
		return cid.toBytes();
	}

}
