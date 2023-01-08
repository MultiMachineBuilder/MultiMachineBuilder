/**
 * 
 */
/**
 * The ENTIRE MultiMachineBuilder base game
 * 
 * It includes several modules:
 * <ul>
 * 	<li>BEANS - interfaces adding simple functionality to blocks and items</li>
 *  <li>DATA - contains game data (sound, textures, settings, language etc...</li>
 *  <li>DEBUG - contains logging utilities (they are heavily used)</li>
 *  <li>ERRORS - contains utilities for handling errors</li>
 *  <li>FILES - contains I/O and file utilities</li>
 *  <li>GEOM - contains geometric calculations (matrices, shapes, vectors, mathematical models etc...</li>
 *  <li>GL - contains OpenGL functionality (2D and 3D graphics, shaders, contexts, texture atlases etc...</li>
 *  <li>GRAPHICS - contains simple graphics (texture generation, 9-patch images, color mappers and crossed boxes)</li>
 *  <li>LAMBDAS - contains lambda functions and functional programming utilities</li>
 *  <li>MENU - contains GUI components and GUIs</li>
 *  <li>MODS - contains modloading and mod data utilities</li>
 *  <li>WORLD (by far the largest) - everything related to the world and gameplay, indcluding GUIs</li>
 * </ul>
 * @author oskar
 */
@javax.annotation.ParametersAreNonnullByDefault
package mmb;