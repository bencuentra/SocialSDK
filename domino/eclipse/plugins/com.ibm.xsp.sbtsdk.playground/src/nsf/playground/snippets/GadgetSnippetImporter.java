package nsf.playground.snippets;

import lotus.domino.Database;
import lotus.domino.Document;
import nsf.playground.beans.GadgetSnippetBean;

import com.ibm.sbt.playground.assets.Asset;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.NodeFactory;
import com.ibm.sbt.playground.assets.opensocial.GadgetSnippet;
import com.ibm.sbt.playground.assets.opensocial.GadgetSnippetNodeFactory;
import com.ibm.sbt.playground.vfs.VFSFile;

/**
 * Class for importing OpenSocial Gadget Snippets.
 * 
 * @author priand
 *
 */
public class GadgetSnippetImporter extends AssetImporter {
	
	public static final String TYPE = "gadget";
	public static final String FORM = GadgetSnippetBean.FORM;
	
	public GadgetSnippetImporter(Database db) {
		super(db);
	}
	
	protected String getAssetType() {
		return TYPE;
	}

	protected String getAssetForm() {
		return FORM;
	}

	protected NodeFactory getNodeFactory() {
		return new GadgetSnippetNodeFactory();
	}

	@Override
	protected void saveAsset(ImportSource source, VFSFile root, AssetNode node, Asset asset) throws Exception {
		GadgetSnippet snippet = (GadgetSnippet)asset;
		Document doc = getDatabase().createDocument();
		try {
			setItemValue(doc,"Form", FORM);
			setItemValue(doc,"Author", doc.getParentDatabase().getParent().getEffectiveUserName()); // Should we make this private (reader field)?
			setItemValue(doc,"Id", node.getUnid());
			setItemValue(doc,"Category", node.getCategory());
			setItemValue(doc,"Name", node.getName());
			setItemValue(doc,"FilterRuntimes", snippet.getProperty("runtimes"));
			setItemValue(doc,"Description", snippet.getProperty("description"));
			setItemValue(doc,"Tags", snippet.getProperty("tags"));
			setItemValue(doc,"ImportSource", source.getName());
			setItemValueRichText(doc,"Gadget", snippet.getGadgetXml());
			setItemValueRichText(doc,"Html", snippet.getHtml());
			setItemValueRichText(doc,"Css", snippet.getCss());
			setItemValueRichText(doc,"JavaScript", snippet.getJs());
			setItemValueRichText(doc,"Json", snippet.getJson());
			snippet.getProperties().remove("endpoints");
			snippet.getProperties().remove("description");
			setItemValueRichText(doc,"Properties", snippet.getPropertiesAsString());
			setItemValueRichText(doc,"Documentation", snippet.getDocHtml());

			doc.save();
		} finally {
			doc.recycle();
		}
	}
}
