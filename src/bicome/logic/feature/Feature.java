package bicome.logic.feature;
/**
 * @author Ege Balcioglu
 * @version 23.4.2018
 */
import bicome.logic.genotype.Genotype;
import java.util.HashMap;
import java.util.function.*;
import bicome.logic.attribute.Attribute;

public class Feature
{
   // properties
   private Genotype genotype;
   private FeatureBase base;
   private HashMap< String, Double > multipliers;
   
   // constructors
   public Feature( FeatureBase base, Genotype genotype )
   {
      this.genotype = genotype;
      this.base = base;
      this.multipliers = base.getMultipliers( this.genotype );
   }
   
   // methods
   public Genotype getGenotype()
   {
      return this.genotype;
   }
   
   public final HashMap< String, Double > getMultipliers()
   {
      return multipliers; 
   }
   
   public FeatureBase getBase()
   {
      return base;
   }
   
   /**
    * Checks if two features are equal to each other.
    * Two features are deeply equal
    * if they have the same base and the same phenotype.
    * <p>This is an unorthodox equals method to be used implicitly in Organism class's 
    * setAttributesFromEnvironment() method, and Environment's filter() method.
    * It ensures that the multipliers that is returned by the getMultipliers() 
    * method of EnvironmentalCondition class is properly multiplied with the 
    * organism's correct features' multipliers. To do this, this method 
    * implicitly converts the genotype into a phenotype and then compares 
    * the phenotypes of the two features in question.
    * @param other the other object to be compared with this one
    * @return returns true if the other object is a Feature that is the same type
    * and bears the same phenotype as this one.
    * @see bicome.logic.world.Organism Organism
    * @see bicome.logic.world.World World
    * @see bicome.logic.environment.Environment Environment
    * @see bicome.logic.environment.EnvironmentalCondition EnvironmentalCondition
    */
   @Override
   public boolean equals( Object other )
   {
      if ( other != null && other instanceof Feature &&
          this.getBase().equals( ( (Feature) other ).getBase() ) )
      {
         Genotype g1;
         Genotype g2;
         g1 = this.getGenotype();
         g2 = ( (Feature) other ).getGenotype();
         // Implicit conversion of genotypes to phenotypes
         if ( g1.equals( g2 ) )
            return true;
         if ( g1.equals( Genotype.DOMINANT_HOMOZYGOTE ) 
                && g2.equals( Genotype.DOMINANT_HETEROZYGOTE ) )
            return true;
         if ( g1.equals( Genotype.DOMINANT_HETEROZYGOTE )
                && g2.equals( Genotype.DOMINANT_HOMOZYGOTE ) )
            return true;
      }
      return false;
   }
   
   /**
    * Checks if two features are deeply equal to each other. Two features are deeply equal
    * if they have the same base and the same genotype. 
    * @param other the other object to be compared with this one
    * @return returns true if the other object is a Feature that is the same type
    * and bears the same genotype as this one.
    */
   public boolean deepEquals( Object other )
   {
       return this.equals(other) && this.genotype == ( (Feature) other ).genotype;
   }
   
   public void multiply( Double externalMultiplier )
   {
      this.getMultipliers().replaceAll( new BiFunction< String, Double, Double >() {
         public Double apply( String attributeType, Double internalMultiplier )
         {
            return internalMultiplier * externalMultiplier;
         }
      } );
   }
}