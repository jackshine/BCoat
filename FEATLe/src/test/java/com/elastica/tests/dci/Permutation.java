package com.elastica.tests.dci;

class Permutation {

	/* arr[]  ---> Input Array
    data[] ---> Temporary array to store current combination
    start & end ---> Staring and Ending indexes in arr[]
    index  ---> Current index in data[]
    r ---> Size of a combination to be printed */
	static void combinationUtil(String country,String arr[], int n, int r, int index,
			String data[], int i)
	{
		// Current combination is ready to be printed, print it
		if (index == r)
		{
			System.out.print(country+"_");
			for (int j=0; j<r; j++)
				System.out.print(data[j]+"_");
			System.out.println(".txt");
			return;
		}

		// When no more elements are there to put in data[]
		if (i >= n)
			return;

		// current is included, put next at next location
		data[index] = arr[i];
		combinationUtil(country, arr, n, r, index+1, data, i+1);

		// current is excluded, replace it with next (Note that
		// i+1 is passed, but index is not changed)
		combinationUtil(country, arr, n, r, index, data, i+1);
	}

	// The main function that prints all combinations of size r
	// in arr[] of size n. This function mainly uses combinationUtil()
	static void printCombination(String country, String arr[], int n, int r)
	{
		// A temporary array to store all combination one by one
		String data[]=new String[r];

		// Print all combination using temprary array 'data[]'
		combinationUtil(country, arr, n, r, 0, data, 0);
	}


	public static void combinationGenerator(String country, String[] terms) {
		int n = terms.length;
		for(int i=1;i<terms.length+1;i++){
			printCombination(country, terms, n, i);
		}
	}

	/*Driver function to check for above function*/
	public static void main (String[] args) {
		String country="Argentina";
		String terms[] = {"DNINo","TaxNo","Name","Age","DOB","Address","Email","Phone"};
		String country1="Australia";
		String terms1[]={"MedicareNo","TaxNo","Name","Age","DOB","Address","Email","Phone","DL"};
		String country2="Belgium";
		String terms2[]={"NationalIDNo","RegisterNo","Name","Age","DOB","Address","Email","Phone","DL"};
		String country3="Brazil";
		String terms3[]={"NationalIDNo","TaxNo","Name","Age","DOB","Address","Email","Phone"};
		String country4="China";
		String terms4[]={"PRCNo","Name","Age","DOB","Address","Email","Phone"};
		String country5="Japan";
		String terms5[]=	{"PersonalNumber","SIN","Name","Age","DOB","Address","Email","Phone","DL"};
		String country6="Mexico";
		String terms6[]={"CURP","RFC","Name","Age","DOB","Address","Email","Phone"};
		String country7="Poland";
		String terms7[]=	{"NationalIDNo","PESEL","REGON","TaxNo","Name","Age","DOB","Address","Email","Phone"};
		String country8="SouthKorea";
		String terms8[]=	{"RRC","Name","Age","DOB","Address","Email","Phone"};
		String country9="Spain";
		String terms9[]=		{"NationalIDNo","SSN","TaxNo","Name","Age","DOB","Address","Email","Phone","DL"};
		String country10="UK";
		String terms10[]={"UTR","NINO","NHS","Name","Age","DOB","Address","Email","Phone","DL"}; 

		combinationGenerator(country, terms);
		combinationGenerator(country1, terms1);
		combinationGenerator(country2, terms2);
		combinationGenerator(country3, terms3);
		combinationGenerator(country4, terms4);
		combinationGenerator(country5, terms5);
		combinationGenerator(country6, terms6);
		combinationGenerator(country7, terms7);
		combinationGenerator(country8, terms8);
		combinationGenerator(country9, terms9);
		combinationGenerator(country10, terms10);





	}

}