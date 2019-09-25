# SOM_GeometryProject
A project demonstrating the uses of a SOM to find geometrical constructions such as lines, planes and spheres in a point cloud.  This project is an Eclipse project.

A video illustrating some of the functionality of this project can be found here : 

https://www.dropbox.com/s/fmaeocifryhva15/SOM_GeomDemo1.mov?dl=0


This project is intended to illustrate the use of a SOM in isolating underlying geometric objects within a point cloud.  If we are given a cloud of points and told that there are some unknown quantity of lines (in 2D or 3D), planes or spheres that contain those points upon their surfaces (in the case of spheres) this project uses a SOM to determine the number and, more importantly, the nature of those underlying geometric primitives.

Each of the primitives (lines, planes and spheres) can be represented in 2 ways, and both ways are used in the project for different purposes.  The first way is as some number of points - 2 unique points will identify a line, 3 unique non-colinear points are needed for a plane, and 4 unique, non-coplanar points for a sphere.  In this project, primitive objects are built by uniformly sampling the possible point space for the required number of points, making sure that the necessary object-related restrictions are met (uniqueness, non-colinarity or co-planarity).  The point set representing an object is then processed appropriate to synthesize a primitive shape.

The second method of representation used in the project is object-type specific - lines are uniquely represented as a point and a direction vector, planes as a point and a normal, and spheres as a point and a radius.  For lines and planes, the points used are defined as the unique point on the line or plane closest to the origin, so that if two objects are the same, their representations will also be the same.  This method is used both to synthesize samples on each object and to represent objects as training examples.

To illustrate the capabilities of the SOM, a point cloud must be synthesised.  There are 4 different environments in the project to manage lines in 2D, and lines, planes and spheres in 3D.  Each of these worlds allow for the generation of some number of primitive objects to be randomly generated, as well as some number of uniformly chosen random surface samples - points that lie on the surface of the line, plane or sphere - per object.  These surface samples are the point cloud that the SOM will learn.   

Training examples are synthesized by sampling the point cloud to build object proposals appropriate to the object type being searched for.  All training examples are unique - no duplicate sample point sets are allowed.  If all of the samples for a particular training object come from the same ground truth object, then we call this is a "true" object, while those that do not are "false" objects.  Note : the SOM has no knowledge of true and false objects, only of proposals; this information is what the SOM will provide once it is trained.

As stated above, the object's representation used to build the training data was defined in such a way so that any object proposals that are built from samples originating from the same object will be represented by the same training data values.  If we build enough training examples, we will have more "true" object proposals than "false" ones, since the probability of multiple, unique point sets being drawn that each define the same object that is not actually represented in the underlying geometry is expected to be small.  

Once the SOM is trained, each training example is then mapped to the appropriate node it most closely matches in the SOM, called its BMU (best matching unit).  Those nodes that have the largest population of training examples mapped to them represent the underlying objects represented within the point cloud.

One challenge to this process is determining how many training examples are necessary.  We want a sufficient number of training examples such that all the underlying "true" objects are represented with some high probability, while not having so many that the training of the SOM is intractable.  This probability is defined by a multivariate hypergeometric distribution, similar to drawing balls from an urn containing many (>2) different colors - specifically, m + 1 colors, where m is the number of underlying objects we have. 

Let s be the number of samples for each object (let's assume its the same # of samples from each object), and n = m * s is the size of the point cloud; furthermore, let's assume g is the "genus" of the object, the number of unique points required to define a particular geometric primitive. 

Our point cloud can provide N = (n choose g) possible proposal objects. There are k = (s choose g) possible proposals that can be built that reprsent a "true" object (all their samples come from the same underlying object); m*k proposals possible from our point cloud that represent one of the m true objects, out of the N proposals possible in from the point cloud.

While we can solve for the number of samples we need to draw, T, to, with some probability p, have at least one proposal from each underlying true object using the multivariate hypergeometric distribution equation, where : 

p = ( k^m * (N-m  choose T-m)) / (N choose T)

Unfortunately, this calculation quickly becomes intractable for any reasonable amount of samples, particularly with panes and spheres.  Furthermore, it requires information we may not have, such as the number of underlying objects, and the number of samples per object.  

Thankfully we can make some simpifying assumptions to build more reasonable calculations that will still provide reasonable estimates for target sample set size.

For one thing, since N >> T we can relax the "without replacement" restriction and, if we can estimate the probabilities of drawing a single "true" object of each object type, instead of using a multivariate hypergeometric distribution, we can use a multinomial distribution to determine the number of draws we want to have every underlying "true" object represented with some probability p, where : 

p  = (T! / (T-m)!) * prod(p_i)  for i = 1->m+1 

where prod(p_i)  for i = 1->m+1  represents the products of the probabilities of drawing each of the m+1 possible proposal types (the m true objects and one more representing the false objects).




