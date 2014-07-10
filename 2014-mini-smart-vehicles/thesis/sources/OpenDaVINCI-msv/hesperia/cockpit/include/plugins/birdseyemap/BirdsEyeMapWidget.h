/*
 * OpenDaVINCI.
 *
 * This software is open source. Please see COPYING and AUTHORS for further information.
 */

#ifndef PLUGINS_BIRDSEYEMAP_BIRDSEYEMAPWIDGET_H_
#define PLUGINS_BIRDSEYEMAP_BIRDSEYEMAPWIDGET_H_

#include <map>
#include <string>

#include "QtIncludes.h"

#include "core/base/Mutex.h"
#include "core/base/TreeNode.h"
#include "core/wrapper/StringComparator.h"

#include "plugins/birdseyemap/CameraAssignableNodesListener.h"
#include "plugins/birdseyemap/BirdsEyeMapMapWidget.h"
#include "plugins/birdseyemap/SelectableNodeDescriptor.h"
#include "plugins/birdseyemap/SelectableNodeDescriptorTreeListener.h"

namespace cockpit {
    namespace plugins {
        namespace birdseyemap {

            /**
             * This class is the outer container for the 2D scene graph viewer
             * implemented in BirdsEyeMapMapWidget and a tree like data
             * structure on its right hand side.
             */
            class BirdsEyeMapWidget : public QWidget, public core::io::ContainerListener, public CameraAssignableNodesListener, public SelectableNodeDescriptorTreeListener {

                Q_OBJECT

                private:
                    /**
                     * "Forbidden" copy constructor. Goal: The compiler should warn
                     * already at compile time for unwanted bugs caused by any misuse
                     * of the copy constructor.
                     */
                    BirdsEyeMapWidget(const BirdsEyeMapWidget &/*obj*/);

                    /**
                     * "Forbidden" assignment operator. Goal: The compiler should warn
                     * already at compile time for unwanted bugs caused by any misuse
                     * of the assignment operator.
                     */
                    BirdsEyeMapWidget& operator=(const BirdsEyeMapWidget &/*obj*/);

                public:
                    /**
                     * Constructor.
                     *
                     * @param plugIn Reference to the plugin to which this widget belongs.
                     * @param prnt Pointer to the parental widget.
                     */
                    BirdsEyeMapWidget(const PlugIn &plugIn, QWidget *prnt);

                    virtual ~BirdsEyeMapWidget();

                    virtual void nextContainer(core::data::Container &c);

                    virtual void update(core::base::TreeNode<SelectableNodeDescriptor> *node);

                    virtual void updateListOfCameraAssignableNodes(const vector<hesperia::scenegraph::SceneNodeDescriptor> &list);

                private slots:
                    /**
                     * This method is called whenever an item in the list changes
                     * its state.
                     *
                     * @param item Item that changed.
                     * @param column Number of column.
                     */
                    void itemChanged(QTreeWidgetItem *item, int column);

                    /**
                     * This method is called whenever an item in the combobox
                     * for the assignable camera changes its state.
                     *
                     * @param item Item that changed.
                     */
                    void selectedItemChanged(const QString &item);

                    /**
                     * This method is called whenever zoom level has been changed.
                     *
                     * @param val New value.
                     */
                    void changeZoom(int val);

                    /**
                     * This method is called whenever reset trace button is released.
                     */
                    void resetTrace();

                private:
                    BirdsEyeMapMapWidget *m_birdsEyeMapMapWidget;

                    QComboBox *m_cameraSelector;
                    core::base::Mutex m_cameraAssignableNodesUpdateMutex;
                    bool m_cameraAssignableNodesUpdate;
                    map<string, hesperia::scenegraph::SceneNodeDescriptor, core::wrapper::StringComparator> m_mapOfSceneNodeDescriptors;

                    QSpinBox *m_zoomLevel;

                    QTreeWidget *m_textualSceneGraph;
                    core::base::Mutex m_textualSceneGraphRootUpdateMutex;
                    bool m_textualSceneGraphRootUpdate;
                    core::base::Mutex m_selectableNodeDescriptorTreeMutex;
                    core::base::TreeNode<SelectableNodeDescriptor> *m_selectableNodeDescriptorTree;

                    /**
                     * This method updates the tree of SelectableNodeDescriptors.
                     *
                     * @param name name of the NodeDescriptor to be updated.
                     * @param enabled True if the NodeDescriptor is enabled.
                     */
                    void updateEntry(core::base::TreeNode<SelectableNodeDescriptor> *node, const string &name, const bool &enabled);
            };
        }
    }
} // plugins::birdseyemap

#endif /*PLUGINS_BIRDSEYEMAP_BIRDSEYEMAPWIDGET_H_*/
