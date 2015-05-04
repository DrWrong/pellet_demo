package tk.drwrong.pallet.main;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import java.util.Iterator;



/**
* An example program that tests the DL-safe rules example from Table 3 in the
* paper: B. Motik, U. Sattler, R. Studer. Query Answering for OWL-DL with
* Rules. Proc. of the 3rd International Semantic Web Conference (ISWC 2004),
* Hiroshima, Japan, November, 2004, pp. 549-563
* 
* @author Evren Sirin
*/
public class Main {
	public static void main(String[] args) {
		String onturl = "http://localhost:8000/dl-safe.owl";
		String ont = "http://owldl.com/ontologies/dl-safe.owl";

		OntModel model = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC, null );
		model.read( onturl );

		ObjectProperty sibling = model.getObjectProperty( ont + "#sibling" );
		if (sibling == null) {
			System.out.println("null pointer");
			return;
		}

		OntClass BadChild = model.getOntClass( ont + "#BadChild" );
		OntClass Child = model.getOntClass( ont + "#Child" );

		Individual Abel = model.getIndividual( ont + "#Abel" );
		Individual Cain = model.getIndividual( ont + "#Cain" );
		Individual Remus = model.getIndividual( ont + "#Remus" );
		Individual Romulus = model.getIndividual( ont + "#Romulus" );

		model.prepare();
		
//      for(Iterator<Statement> iterator = model.listStatements();iterator.hasNext();){
//    	Statement statement = iterator.next();
//    	Resource subject = statement.getSubject();
//    	Property predicate = statement.getPredicate();
//    	RDFNode object = statement.getObject();
//        System.out.print(subject.toString());
//        System.out.print(" " + predicate.toString() + " ");
//        if (object instanceof Resource) {
//           System.out.print(object.toString());
//        } else {
//            // object is a literal
//            System.out.print(" \"" + object.toString() + "\"");
//        }
//
//        System.out.println(" .");
//
//    }
		// Cain has sibling Abel due to SiblingRule
		printPropertyValues( Cain, sibling );
		// Abel has sibling Cain due to SiblingRule and rule works symmetric
		printPropertyValues( Abel, sibling );
		// Remus is not inferred to have a sibling because his father is not
		// known
		printPropertyValues( Remus, sibling );
		// No siblings for Romulus for same reasons
		printPropertyValues( Romulus, sibling );

		// Cain is a BadChild due to BadChildRule
		printInstances( BadChild );
		// Cain is a Child due to BadChildRule and ChildRule2
		// Oedipus is a Child due to ChildRule1 and ChildRule2 combined with the
		// unionOf type
		printInstances( Child );
	}

	public static void printPropertyValues(Individual ind, Property prop) {
		System.out.print( ind.getLocalName() + " has " + prop.getLocalName() + "(s): " );
		printIterator( ind.listPropertyValues( prop ) );
	}

	public static void printInstances(OntClass cls) {
		System.out.print( cls.getLocalName() + " instances: " );
		printIterator( cls.listInstances() );
	}

	public static void printIterator(ExtendedIterator i) {
		if( !i.hasNext() ) {
			System.out.print( "none" );
		}
		else {
			while( i.hasNext() ) {
				Resource val = (Resource) i.next();
				System.out.print( val.getLocalName() );
				if( i.hasNext() )
					System.out.print( ", " );
			}
		}
		System.out.println();
	}
}

