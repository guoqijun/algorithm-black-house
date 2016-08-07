package leetcode;

import java.util.HashMap;
import java.util.HashSet;

/* There are a total of n courses you have to take, labeled from 0 to n - 1.
 * Some courses may have prerequisites, for example to take course 0 you have to first take course 1, 
 * which is expressed as a pair: [0,1]
 * Given the total number of courses and a list of prerequisite pairs, is it possible for you to finish all courses?
 * For example:
 * 2, [[1,0]]
 * There are a total of 2 courses to take. To take course 1 you should have finished course 0. So it is possible.
 * 2, [[1,0],[0,1]]
 * There are a total of 2 courses to take. To take course 1 you should have finished course 0, and to take course 0 you should also have finished course 1. So it is impossible.
 * Note:
 * The input prerequisites is a graph represented by a list of edges, not adjacency matrices. Read more about how a graph is represented.
 */
//TLE
public class CourseSchedule {
	public boolean canFinish(int numCourses, int[][] prerequisites) {
		HashMap<Integer, Integer> map = new HashMap<>();
		for (int edge[] : prerequisites) {
			map.put(edge[0], edge[1]);
		}
		for (int from : map.keySet()) {
			HashSet<Integer> set = new HashSet<>();
			while (map.containsKey(from)) {
				if (set.contains(from))
					return false;
				set.add(from);
				from = map.get(from);
			}
		}
		return true;
	}

	public static void main(String[] args) {
		int[][] pre = {};
	}
}
